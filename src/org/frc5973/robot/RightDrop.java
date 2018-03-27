package org.frc5973.robot;

import org.strongback.Strongback;
import org.strongback.command.CommandGroup;
import org.strongback.drive.TankDrive;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;

public class RightDrop extends CommandGroup {
	public RightDrop(TankDrive drive, GyroWrapper gyro) {
		//drop right
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		if(gameData.length() > 0) {
			if(gameData.charAt(0) == 'R') {
				sequentially(new RightCubeRight(drive, gyro));
			}
			else if(gameData.charAt(0) == 'L') {
				sequentially(new RightCubeLeft(drive, gyro));
			}
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
