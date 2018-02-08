/* 
 * Team 5973, Moonshots
 * 2018 - Rhea
 * Software Captain: Sebastian V. Williams
 * test
 */
package org.frc5973.robot;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import org.frc5973.robot.*;
import org.strongback.Strongback;
import org.strongback.SwitchReactor;
import org.strongback.command.Command;
import org.strongback.components.Motor;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.FlightStick;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;
import org.strongback.util.Values;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;

import edu.wpi.first.wpilibj.CameraServer;

// NEW STUFF THIS YEAR
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Robot extends IterativeRobot {

	// Declares the ports for the motors and joysticks
	private static final int JOYSTICK_PORT = 0; // in driver station
	private static final int RMOTOR_FRONT = 3;
	private static final int RMOTOR_REAR = 2;
	private static final int LMOTOR_FRONT = 0;
	private static final int LMOTOR_REAR = 1;

	// ACCURATE TURN TOOLS
	double Kp = 0.03;
	boolean done = false;
	int counter = 0;
	int counter2 = 0;
	private ADXRS450_Gyro gyro;

	// Declares the TankDrive reference along with the ContinuousRange objects
	private TankDrive drive;
	private ContinuousRange driveSpeed;
	private ContinuousRange turnSpeed;
	private ContinuousRange turnSpeed2;

	// We moved this up here so we can output this variable in the teleop
	protected ContinuousRange sensitivity;

	// Used to limit and format the number of console outputs
	private int filter = 0;
	private String pattern = "###.###";
	private DecimalFormat myFormat = new DecimalFormat(pattern);
	private double sen;
	

	// PNEUMATICS
//	DoubleSolenoid exDub = new DoubleSolenoid(2, 3);
//	Compressor c = new Compressor(0);

	// AUTONOMOUS MODE SELECTOR
	Command autonomousCommand;
	@SuppressWarnings("rawtypes")
	SendableChooser autoChooser;

	@SuppressWarnings("unchecked")
	@Override
	public void robotInit() {
		Strongback.configure().recordNoData().recordNoCommands().recordNoEvents().useExecutionPeriod(200,
				TimeUnit.MILLISECONDS);
		// Sets up the two cameras, one facing forward and once facing backwards
		// CameraServer.getInstance().startAutomaticCapture(0);
		// CameraServer.getInstance().startAutomaticCapture(1);

		// ENABLE COMPRESSOR
//		c.start();

		// Set up the robot hardware ...
		Motor left_front = Hardware.Motors.victorSP(LMOTOR_FRONT).invert(); // left rear
		Motor left_rear = Hardware.Motors.victorSP(LMOTOR_REAR).invert(); // left front
		// DoubleToDoubleFunction SPEED_LIMITER = Values.limiter(-0.1, 0.1);
		Motor right_front = Hardware.Motors.victorSP(RMOTOR_FRONT); // right rear
		Motor right_rear = Hardware.Motors.victorSP(RMOTOR_REAR);
		gyro = new ADXRS450_Gyro();// right front

		Motor left = Motor.compose(left_front, left_rear);
		Motor right = Motor.compose(right_front, right_rear);
		

		drive = new TankDrive(left, right);
		// Set up the human input controls for teleoperated mode. We want to use
		// the Logitech Attack 3D's throttle as a
		// "sensitivity" input to scale the drive speed and throttle, so we'll
		// map it from it's native [-1,1] to a simple scale
		// factor of [0,1] ...
		FlightStick joystick = Hardware.HumanInterfaceDevices.logitechExtreme3D(JOYSTICK_PORT);

		SwitchReactor reactor = Strongback.switchReactor();
		sensitivity = joystick.getThrottle().map(t -> ((t + 1.0) / 2.0));
		sensitivity = joystick.getThrottle().map(Values.mapRange(-1.0, 1.0).toRange(0.0, 1.0));
		driveSpeed = joystick.getPitch().scale(sensitivity::read).invert(); // scaled
		turnSpeed = joystick.getRoll().scale(sensitivity::read);
		turnSpeed2 = joystick.getYaw().scale(sensitivity::read);
		// scaled and
		// inverted
		reactor.onTriggered(joystick.getButton(7), () -> switchControls());

		// PNEUMATIC CONTROLS
//		reactor.onTriggered(joystick.getButton(9), () -> exDub.set(DoubleSolenoid.Value.kOff));
//		reactor.onTriggered(joystick.getButton(10), () -> exDub.set(DoubleSolenoid.Value.kForward));
//		reactor.onTriggered(joystick.getButton(11), () -> exDub.set(DoubleSolenoid.Value.kReverse));

		autoChooser = new SendableChooser();
		autoChooser.addDefault("Go forward (2s)", new TimedDriveCommand(drive, gyro, .2, false, 2000));
		autoChooser.addObject("Turn 180", new AngularTurnCommand(drive, gyro, .2, false, 180));
		autoChooser.addObject("Fetch", new GroupForwardLeft(drive, gyro));
		SmartDashboard.putData("Autonomous Mode Selector", autoChooser);
		SmartDashboard.putNumber("Gyro", 0.0);
	}

	public void autonomousInit() {
		// Start Strongback functions ...
		Strongback.start();
		done = false;

		gyro.reset();
		
		autonomousCommand = (Command) autoChooser.getSelected();
		Strongback.submit(autonomousCommand);
	}

	public void autonomousPeriodic() {
		SmartDashboard.putNumber("Gyro", gyro.getAngle());
		
	}

	@Override
	public void teleopInit() {
		// Kill anything running if it is ...
		Strongback.disable();
		// Start Strongback functions ...
		Strongback.start();
		// c.setClosedLoopControl(true);
	}

	@Override
	public void teleopPeriodic() {
		drive.arcade(driveSpeed.read(), turnSpeed.read());
		SmartDashboard.putNumber("Gyro", gyro.getAngle());
		
		
	}

	@Override
	public void disabledInit() {
		// Tell Strongback that the robot is disabled so it can flush and kill
		// commands.
		Strongback.disable();
		// c.setClosedLoopControl(false);
	}

	public void switchControls() {
		driveSpeed = driveSpeed.invert();
		// turnSpeed = turnSpeed.invert();
		// turnSpeed2 = turnSpeed2.invert();
	}
	
}
