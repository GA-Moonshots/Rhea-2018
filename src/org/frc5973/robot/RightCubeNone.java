package org.frc5973.robot;

import org.strongback.command.CommandGroup;
import org.strongback.drive.TankDrive;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class RightCubeNone extends CommandGroup {
	public RightCubeNone(TankDrive drive, GyroWrapper gyro) {
		sequentially(new TimedDriveCommand(drive, gyro, .3, false, 4000));
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
