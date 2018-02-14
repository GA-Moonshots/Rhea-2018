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
import edu.wpi.cscore.UsbCamera;

/*
 * WPI is the main library that FRC uses. It's kind of complicated, so we use another
 * library to help. We are using Pneumatics so we have to import directly from
 * WPI lib
 */
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.CameraServer;

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
 * @author Sebastian Williams
 * Our main class for our robot. This class handles everything operation wise and is
 * run at the competition. This is the 'brain' of our robot and every other class
 * has to work through this one
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class Robot extends IterativeRobot {

	// Declares the ports for the motors and joysticks
	private static final int JOYSTICK_PORT = 0; // in driver station
	private static final int RMOTOR_FRONT = 3;
	private static final int RMOTOR_REAR = 2;
	private static final int LMOTOR_FRONT = 0;
	private static final int LMOTOR_REAR = 1;

	// Declares our Gryo using the GyroWrapper class wecreated
	private GyroWrapper gyro;

	// Declares the TankDrive reference along with the ContinuousRange objects
	private TankDrive drive;
	private ContinuousRange driveSpeed;
	private ContinuousRange turnSpeed;

	// We moved this up here so we can output this variable in the teleop
	protected ContinuousRange sensitivity;
	

	// Declares our Compressor and DoubleSolenoid for pneumatics 
	DoubleSolenoid exDub = new DoubleSolenoid(2, 3);
	Compressor c = new Compressor(0);

	// Declares our SendableChooser and Command for our autonomous mode selector
	Command autonomousCommand;
	SendableChooser autoChooser;

	/**
	 * The initialization method for our robot which is called when we turn it on. 
	 * This method instantiates all the variables we created above and performs
	 * a few other, necessary startup functions.
	 * @Override
	 */
	@Override
	public void robotInit() {
		
		// Sets up a logging system through Strongback (not sure about this)
		Strongback.configure().recordNoData().recordNoCommands().recordNoEvents().useExecutionPeriod(200,
				TimeUnit.MILLISECONDS);
		
		// Sets up the two cameras, one facing forward and once facing backwards
		
		UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
		UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture(1);
		camera1.setResolution(160,120);
		camera2.setResolution(160,120);

		// Enables compressor and immediately activates the solenoid to grasp the power cube
		c.start();
		c.setClosedLoopControl(true);
		exDub.set(DoubleSolenoid.Value.kForward);

		// Sets up the motors
		Motor left_front = Hardware.Motors.victorSP(LMOTOR_FRONT).invert();
		Motor left_rear = Hardware.Motors.victorSP(LMOTOR_REAR).invert(); 
		Motor right_front = Hardware.Motors.victorSP(RMOTOR_FRONT);
		Motor right_rear = Hardware.Motors.victorSP(RMOTOR_REAR);
		
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

		// Maps the Pneumatics controls to the buttons on the joystick
		reactor.onTriggered(joystick.getButton(9), () -> exDub.set(DoubleSolenoid.Value.kOff));
		reactor.onTriggered(joystick.getButton(10), () -> exDub.set(DoubleSolenoid.Value.kForward));
		reactor.onTriggered(joystick.getButton(11), () -> exDub.set(DoubleSolenoid.Value.kReverse));

		/*
		 * Sets up the autonomous mode chooser and lists the 9 possible options. 
		 * We use ShuffleBoard to display the options and different data
		 * streams for our robot. ShuffleBoard inherits from SmartDashboard
		 */
		autoChooser = new SendableChooser();
		
		autoChooser.addDefault("Start Left-Drop Left", new LeftCubeLeft(drive, gyro));
		autoChooser.addObject("Start Left-Drop Right", new LeftCubeRight(drive, gyro));
		autoChooser.addObject("Start Left-Don't Drop", new LeftCubeNone(drive, gyro));
		
		autoChooser.addObject("Start Right-Drop Left", new RightCubeLeft(drive, gyro));
		autoChooser.addObject("Start Right-Drop Right", new RightCubeRight(drive, gyro));
		autoChooser.addObject("Start Right-Don't Drop", new RightCubeNone(drive, gyro));
		
		autoChooser.addObject("Start Middle-Drop Left", new MiddleCubeLeft(drive, gyro));
		autoChooser.addObject("Start Middle-Drop Right", new MiddleCubeRight(drive, gyro));
		autoChooser.addObject("Start Middle-Don't Drop", new MiddleCubeNone(drive, gyro));
		
		SmartDashboard.putData("Autonomous Mode Selector", autoChooser);
	}

	/**
	 * This method is called at the beginning of autonomous mode and setups up the robot
	 * for the period. It also contains the logic to read from the SmartDashabord
	 */
	public void autonomousInit() {
		// Start Strongback functions ...
		Strongback.start();

		// Resets the Gyro to Zero degrees
		gyro.reset();
		
		// Reads and submits (to the scheduler) the chose command from the SmartDhashboard
		autonomousCommand = (Command) autoChooser.getSelected();
		Strongback.submit(autonomousCommand);
	}

	/**
	 * We used to use this for our autonomous logic but we use the Strongback
	 * scheduler instead
	 * @deprecated
	 */
	@Deprecated
	public void autonomousPeriodic() {

	}

	/**
	 * This method is called at the beginning of autonomous mode and sets up the 
	 * robot for the period.
	 * @Override
	 */
	@Override
	public void teleopInit() {
		
		// Kill any commands that might be lefotover from autonomous
		Strongback.disable();
		
		// Restart Strongback functions
		Strongback.start();
		
		// Uses the built in features to regulate pressure in the Compressor (not sure about this)
		c.setClosedLoopControl(true);

	}

	/**
	 * This method is called many times a second during teleop and reads from the joystick
	 * to determine where and how fast the robot should go.
	 * @Override
	 */
	@Override
	public void teleopPeriodic() {
		
		// Reads how fast and if the robot should turn and passes that to the drive.arcade method
		drive.arcade(driveSpeed.read(), turnSpeed.read());
		
		
	}

	/**
	 * This method is called when the robot is done and flushes anything no longer
	 * needed
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
	 * This method allows the user (through the joystick) to change the direction for
	 * what forward and backwards is
	 */
	public void switchControls() {
		
		// Inverts the drive speed
		driveSpeed = driveSpeed.invert();
	}
	
		
}
