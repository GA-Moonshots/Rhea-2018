package org.frc5973.robot;

import org.strongback.command.CommandGroup;
import org.strongback.drive.TankDrive;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class LeftCubeRight extends CommandGroup {
	public LeftCubeRight(TankDrive drive, GyroWrapper gyro) {
		sequentially(new TimedDriveCommand(drive, gyro, .3, false, 7000),
				new AngularTurnCommand(drive, gyro, .2, false, 90), 
				new TimedDriveCommand(drive, gyro, .3, false, 7000),
				new AngularTurnCommand(drive, gyro, .2, false, -90),
				new TimedDriveCommand(drive, gyro, .3, false, 1800), 
				new AngularTurnCommand(drive, gyro, .2, false, -90),
				new TimedDriveCommand(drive, gyro, .2, false, 1000));
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
