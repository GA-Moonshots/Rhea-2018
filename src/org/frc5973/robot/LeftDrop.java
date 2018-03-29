package org.frc5973.robot;

import org.strongback.Strongback;
import org.strongback.command.CommandGroup;
import org.strongback.components.Motor;
import org.strongback.drive.TankDrive;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;

public class LeftDrop extends CommandGroup {
	public LeftDrop(RobotState robotState, Motor lift_pulley, Motor lift_elevator,TankDrive drive, GyroWrapper gyro, DoubleSolenoid exDub) {
		//drop right
		
		if(Robot.gameData.length() > 0) {
			if(Robot.gameData.charAt(0) == 'R'){
				sequentially(new LeftCubeRight(drive, gyro),
						new ArmCommand(robotState, "mid", lift_pulley, lift_elevator),
						new ArmGrab(exDub));
			}
			
			else if(Robot.gameData.charAt(0) == 'L') {
				sequentially(new LeftCubeLeft(drive, gyro),
						new ArmCommand(robotState, "mid", lift_pulley, lift_elevator),
						new ArmGrab(exDub));
			}
			
		}
		else {
			Strongback.submit(new LeftCubeNone(drive, gyro));
		}

	}
		
}



// TODO
// Left-Cube-Left
// Left-Cube-Right
// Left-Cube-None

// Middle-Cube-Left
// Middle-Cube-Right
// Middle-Cube-None

// Right-Cube-Left
// Right-Cube-Right
// Right-Cube-None
