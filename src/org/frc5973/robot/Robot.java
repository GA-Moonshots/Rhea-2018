/* 
 * Team 5973, Moonshots
 * 2018 - Rhea
 * Software Captain: Sebastian Williams
 */
package org.frc5973.robot;

import org.strongback.Strongback;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Robot extends IterativeRobot {

	private int position;
	
    @Override
    public void robotInit() {
    	position = (int)SmartDashboard.getNumber("Position", 1);
    }

    @Override
    public void teleopInit() {
        // Start Strongback functions ...
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
