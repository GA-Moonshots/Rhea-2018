/* 
 * Team 5973, Moonshots
 * 2018 - Rhea
 * Software Captain: Sebastian Williams
 * 
 * Project Setup: Eclipse using a '.' to delineate subfolders, but
 * our project has dots in its name. I think we set it up incorrectly
 * when we first started FRC, but it hasn't been causing us any problems.
 * Figuring out how to properly organize it is something we should do in
 * the future
 */
package org.frc5973.robot;

// Imported from the Java Library
import java.util.concurrent.TimeUnit;

/*
 * WPI is the main library that FRC uses. It's kind of complicated, so we use another
 * library to help. We are using s so we have to import directly from
 * WPI lib
 */
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.cscore.UsbCamera;

/*
 * Imported from Strongback, a library that inherits from WPILib and streamlines many
 * features. We use Strongback because it makes it easier to use commands
 */
import org.strongback.Strongback;
import org.strongback.SwitchReactor;
import org.strongback.command.Command;
import org.strongback.components.Motor;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.FlightStick;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;
import org.strongback.util.Values;

/**
 * 
 * @author Sebastian Williams Our main class for our robot. This class handles
 *         everything operation wise and is run at the competition. This is the
 *         'brain' of our robot and every other class has to work through this
 *         one
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class Robot extends IterativeRobot {

	// USB port for driver station
	private static final int JOYSTICK_PORT = 0;// in driver station
	// PWM ports on the roboRIO
	private static final int RMOTOR_FRONT = 3;
	private static final int RMOTOR_REAR = 2;
	private static final int LMOTOR_FRONT = 0;
	private static final int LMOTOR_REAR = 1;
	private static final int LIFT_PULLEY = 7;
	private static final int LIFT_ELEVATOR = 4;

	
//	//Declares the ports for the winch and door
//	private static final int WINCH_PORT = 6;
//	private static final int WINCH2_PORT = 8;

	
	

	// Declares our Gryo using the GyroWrapper class wecreated
	private GyroWrapper gyro;

	// Declares our sensors
	// AnalogInput ultra = new AnalogInput(0);
	AnalogInput actuator = new AnalogInput(1);

	// Declares the TankDrive reference along with the ContinuousRange objects
	private TankDrive drive;
	private ContinuousRange driveSpeed;
	private ContinuousRange turnSpeed;

	// We moved this up here so we can output this variable in the teleop
	protected ContinuousRange sensitivity;

	// Declares our Compressor and DoubleSolenoid for s
	private DoubleSolenoid exDub = new DoubleSolenoid(2, 3);
	private Compressor c = new Compressor(0);

	// Making this motor an instance variable so our autonomous mode can access this
	// as well
	private Motor lift_pulley;
	private Motor lift_elevator;

	// Declares our SendableChooser and Command for our autonomous mode selector
	private Command autonomousCommand;
	private SendableChooser autoChooser;
	
	/**
	 * The initialization method for our robot which is called when we turn it on.
	 * This method instantiates all the variables we created above and performs a
	 * few other, necessary startup functions.
	 * 
	 * @Override
	 */
	@Override
	public void robotInit() {
			// Reads and submits (to the scheduler) the chose command from the
			// SmartDhashboard
		
		// Sets up a logging system through Strongback (not sure about this)
		Strongback.configure().recordNoData().recordNoCommands().recordNoEvents().useExecutionPeriod(200,
				TimeUnit.MILLISECONDS);

		// Sets up the two cameras, one facing forward and once facing backwards
		UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
		//UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture(1);
		camera1.setResolution(160, 120);
		//camera2.setResolution(160, 120);

		// Enables compressor and immediately activates the solenoid to grasp the power
		// cube
		//c.start();
		//c.setClosedLoopControl(true);
		
		exDub.set(DoubleSolenoid.Value.kForward);

		// Sets up the motors
		Motor left_front = Hardware.Motors.victorSP(LMOTOR_FRONT).invert();
		Motor left_rear = Hardware.Motors.victorSP(LMOTOR_REAR).invert();
		Motor right_front = Hardware.Motors.victorSP(RMOTOR_FRONT);
		Motor right_rear = Hardware.Motors.victorSP(RMOTOR_REAR);

		// Sets up the motors for the elevator and the grabber
		lift_elevator = Hardware.Motors.victorSP(LIFT_ELEVATOR).invert();
		lift_pulley = Hardware.Motors.victorSP(LIFT_PULLEY).invert();
	
//		Motor winch = Hardware.Motors.victorSP(WINCH_PORT);
//		Motor winch2 = Hardware.Motors.victorSP(WINCH2_PORT);
		
//		Motor winch_compose = Motor.compose(winch, winch2);
		// Instantiates the GyroScope using the GyroWrapper class we created
		gyro = new GyroWrapper();

		// Composes the motors, combing them into two sets
		Motor left = Motor.compose(left_front, left_rear);
		Motor right = Motor.compose(right_front, right_rear);

		// Sets up the TankDrive which is the main Drive we use
		drive = new TankDrive(left, right);

		
		/*
		 * Set up the human input controls for teleoperated mode. We want to use the
		 * Logitech Attack 3D's throttle as a "sensitivity" input to scale the drive
		 * speed and throttle, so we'll map it from it's native [-1,1] to a simple scale
		 * factor of [0,1]
		 */
		FlightStick joystick = Hardware.HumanInterfaceDevices.logitechExtreme3D(JOYSTICK_PORT);
		SwitchReactor reactor = Strongback.switchReactor();
		sensitivity = joystick.getThrottle().map(t -> ((t + 1.0) / 2.0));
		sensitivity = joystick.getThrottle().map(Values.mapRange(-1.0, 1.0).toRange(0.0, 1.0));
		driveSpeed = joystick.getPitch().scale(sensitivity::read);
		turnSpeed = joystick.getRoll().scale(sensitivity::read);

		// Maps the switchControls method to the button on the JoyStick
		reactor.onTriggered(joystick.getButton(7), () -> switchControls());
		
		reactor.onTriggered(joystick.getButton(5), () -> 
		lift_elevator.setSpeed(1));
		reactor.onUntriggered(joystick.getButton(5), () -> 
		lift_elevator.stop());

		reactor.onTriggered(joystick.getButton(3), () -> 
		lift_elevator.setSpeed(-1));
		reactor.onUntriggered(joystick.getButton(3), () -> 
		lift_elevator.stop());
		
		reactor.onTriggered(joystick.getButton(4), () -> 
		lift_pulley.setSpeed(-1));
		reactor.onUntriggered(joystick.getButton(4), () -> 
		lift_pulley.stop());

		reactor.onTriggered(joystick.getButton(6), () -> 
		lift_pulley.setSpeed(1));
		reactor.onUntriggered(joystick.getButton(6), () -> 
		lift_pulley.stop());
		
		reactor.onTriggered(joystick.getButton(11), () -> 
		lift_pulley.setSpeed(-.5));
		reactor.onUntriggered(joystick.getButton(11), () -> 
		lift_pulley.stop());

		reactor.onTriggered(joystick.getButton(12), () -> 
		lift_pulley.setSpeed(.5));
		reactor.onUntriggered(joystick.getButton(12), () -> 
		lift_pulley.stop());

	
//		// Maps the buttons for the elevator control
//		reactor.onTriggered(joystick.getButton(9), () -> lift_elevator.setSpeed(0.5));
//		reactor.onUntriggered(joystick.getButton(9), () -> lift_elevator.stop());
//		// Moves elevator down
//		reactor.onTriggered(joystick.getButton(10), () -> lift_elevator.setSpeed(-0.5));
//		reactor.onUntriggered(joystick.getButton(10), () -> lift_elevator.stop());

//		// Maps the buttons for the grabber control
//		reactor.onTriggered(joystick.getButton(3), () -> lift_pulley.setSpeed(0.5));
//		reactor.onUntriggered(joystick.getButton(3), () -> lift_pulley.stop());
//		// tilting arm down by reeling out pulley
//		reactor.onTriggered(joystick.getButton(4), () -> lift_pulley.setSpeed(-0.5));
//		reactor.onUntriggered(joystick.getButton(4), () -> lift_pulley.stop());
		reactor.onTriggered(joystick.getButton(9), () -> {
			lift_pulley.setSpeed(.3);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("Error here");
				e.printStackTrace();
			}
			lift_pulley.stop();

		});
		
		
		reactor.onTriggered(joystick.getButton(10), () -> {
			lift_pulley.setSpeed(-.3);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("Error here");
				e.printStackTrace();
			}
			lift_pulley.stop();

		});
		
		//Either grabs or releases the box, depending on the state
		reactor.onTriggered(joystick.getButton(1), () -> Strongback.submit(new ArmGrab(exDub)));
		reactor.onTriggered(joystick.getButton(2), () -> Strongback.submit(new ArmRelease(exDub)));

		//Puts the arm to the mid state
//		reactor.onTriggered(joystick.getButton(5), () -> 
//			Strongback.submit(new ArmCommand(customRobotState, "low", lift_pulley, lift_elevator)));
//
//		reactor.onTriggered(joystick.getButton(3), () -> 
//			Strongback.submit(new ArmCommand(customRobotState, "mid", lift_pulley, lift_elevator)));
//		
//		reactor.onTriggered(joystick.getButton(6), () ->
//			Strongback.submit(new ArmCommand(customRobotState, "high", lift_pulley, lift_elevator)));
//		
		
//
//		reactor.onTriggered(joystick.getButton(11), () -> winch_compose.setSpeed(1));
//		reactor.onUntriggered(joystick.getButton(11), () -> winch_compose.stop());
		//Puts the arms to the high state

		
		// Maps the Pneumatics controls to the buttons on the joystick
		// reactor.onTriggered(joystick.getButton(1), () ->
		// exDub.set(DoubleSolenoid.Value.kOff));
		// reactor.onTriggered(joystick.getButton(2), () ->
		// exDub.set(DoubleSolenoid.Value.kForward));
		// reactor.onUntriggered(joystick.getButton(2), () ->
		// exDub.set(DoubleSolenoid.Value.kReverse));

		/*
		 * Sets up the autonomous mode chooser and lists the 9 possible options. We use
		 * ShuffleBoard to display the options and different data streams for our robot.
		 * ShuffleBoard inherits from SmartDashboard
		 */
		

		autoChooser = new SendableChooser();

		autoChooser.addDefault("Drop Left", new DumbCommand("Drop Left"));
		autoChooser.addObject("Drop Right", new DumbCommand("Drop Right"));
		autoChooser.addObject("Drop Middle", new DumbCommand("Drop Middle"));

		autoChooser.addObject("Middle - don't drop", new DumbCommand("Middle - don't drop"));
		autoChooser.addObject("Left - don't drop", new DumbCommand("Left - don't drop"));
		autoChooser.addObject("Right - don't drop", new DumbCommand("Right - don't drop"));

		SmartDashboard.putData("Autonomous Mode Selector", autoChooser);
		
	}

	/**
	 * This method is called at the beginning of autonomous mode and setups up the
	 * robot for the period. It also contains the logic to read from the
	 * SmartDashabord
	 */
	public void autonomousInit() {
		// Start Strongback functions ...
		Strongback.start();
		c.start();
		c.setClosedLoopControl(true);
		gyro.reset();
		
		System.out.println(gyro.getAngle());
		DumbCommand dc = (DumbCommand)autoChooser.getSelected();
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		if(dc.getMessage().equals("Drop Left")) {
			Strongback.submit(new LeftDrop(gameData, lift_pulley, lift_elevator, drive, gyro, exDub));
		}
		
		else if(dc.getMessage().equals("Drop Right")) {
			Strongback.submit(new RightDrop(gameData, lift_pulley, lift_elevator, drive, gyro, exDub));
		}
		
		else if(dc.getMessage().equals("Drop Middle")) {
			Strongback.submit(new MiddleDrop(gameData, lift_pulley, lift_elevator, drive, gyro, exDub));
		}
		
		else if(dc.getMessage().equals("Middle - don't drop")) {
			Strongback.submit(new MiddleCubeNone(drive, gyro));
		}
		
		else if(dc.getMessage().equals("Left - don't drop")) {
			Strongback.submit(new LeftCubeNone(drive, gyro));
		}
		
		else if(dc.getMessage().equals("Right - don't drop")) {
			Strongback.submit(new RightCubeNone(drive, gyro));
		}
	
	}

	/**
	 * We used to use this for our autonomous logic but we use the Strongback
	 * scheduler instead
	 * 
	 * @deprecated
	 */

	public void autonomousPeriodic() {

	}

	/**
	 * This method is called at the beginning of autonomous mode and sets up the
	 * robot for the period.
	 * 
	 * @Override
	 */
	@Override
	public void teleopInit() {
		c.start();
		c.setClosedLoopControl(true);
		// Kill any commands that might be lefotover from autonomous
		Strongback.disable();
		c.setClosedLoopControl(true);
		// Restart Strongback functions
		Strongback.start();

		// Uses the built in features to regulate pressure in the Compressor (not sure
		// about this)

	}

	/**
	 * This method is called many times a second during teleop and reads from the
	 * joystick to determine where and how fast the robot should go.
	 * 
	 * @Override
	 */
	@Override
	public void teleopPeriodic() {

		// Reads how fast and if the robot should turn and passes that to the
		// drive.arcade method
		drive.arcade(driveSpeed.read(), turnSpeed.read());

		// Sets up our ultrasonic sensor
		// SmartDashboard.putNumber("Ultra Distance Reading",
		// ultra.getVoltage()/0.00097);

	}

	/**
	 * This method is called when the robot is done and flushes anything no longer
	 * needed
	 * 
	 * @Override
	 */
	@Override
	public void disabledInit() {

		// Tell Strongback that the robot is disabled so it can flush and kill commands
		Strongback.disable();

		// Stops the compressor
		c.setClosedLoopControl(false);
	}

	/**
	 * This method allows the user (through the joystick) to change the direction
	 * for what forward and backwards is
	 */
	public void switchControls() {

		// Inverts the drive speed
		driveSpeed = driveSpeed.invert();
	}
	

}
