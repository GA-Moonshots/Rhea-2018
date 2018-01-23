<<<<<<< HEAD
/* 
 * Team 5973, Moonshots
 * 2018 - Rhea
 * Software Captain: Sebastian V. Williams
 */
package org.frc5973.robot;

import org.strongback.Strongback;
import org.strongback.command.Command;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;


public class Robot extends IterativeRobot {

	Command autonomousCommand;
	@SuppressWarnings("rawtypes")
	SendableChooser autoChooser;
	
	@SuppressWarnings("unchecked")
	@Override
    public void robotInit() {
    	autoChooser = new SendableChooser();
    	autoChooser.addDefault("Default program", new JustForward());
    	autoChooser.addObject("Right start, right switch", new JustForward());
    }
    
    public void autonomousInit(){
        //Start Strongback functions ...
        Strongback.start();    
        autonomousCommand = (Command) autoChooser.getSelected();
        Strongback.submit(autonomousCommand);
    }
    

    @Override
    public void teleopInit() {
        //Start Strongback functions ...
        Strongback.start();
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }

}
=======
/* 
 * Team 5973, Moonshots
 * 2018 - Rhea
 * Software Captain: Sebastian Williams
 */
package org.frc5973.robot;

import org.strongback.Strongback;
import org.strongback.command.Command;]

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;


public class Robot extends IterativeRobot {

	//SmartDashboard autonomous mode choice setup
	Command autonomousCommand;
	@SuppressWarnings("rawtypes")
	SendableChooser autoChooser;
	
	@SuppressWarnings("unchecked")
	@Override
	//initiaties robot
    public void robotInit() {
    	autoChooser = new SendableChooser();
    	autoChooser.addDefault("Default program", new JustForward());
    	autoChooser.addDefault("Right start, right switch", new JustForward());
    }
    
    public void autonomousInit(){
        //Start Strongback functions ...
        Strongback.start(); 
        //Submits command to Strongback
        autonomousCommand = (Command) autoChooser.getSelected();
        Strongback.submit(autonomousCommand);
    }
    

    @Override
    public void teleopInit() {
        //Start Strongback functions ...
        Strongback.start();
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }

}
>>>>>>> eba0c2526d1a64c16c986d8fba020ffe466d469d
