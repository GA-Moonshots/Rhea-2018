package org.frc5973.robot;

import org.strongback.Strongback;
import org.strongback.command.CommandGroup;
import org.strongback.drive.TankDrive;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;

public class MiddleDrop extends CommandGroup {
	public MiddleDrop(TankDrive drive, GyroWrapper gyro, DoubleSolenoid exDub) {
		//drop right
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		if(gameData.length() > 0) {
			if(gameData.charAt(0) == 'R') {
				sequentially(new MiddleCubeRight(drive, gyro),
						new PneumaticGrab(exDub));
			}
			else if(gameData.charAt(0) == 'L') {
				sequentially(new MiddleCubeLeft(drive, gyro),
						new PneumaticGrab(exDub));
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
