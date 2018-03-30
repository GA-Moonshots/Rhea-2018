package org.frc5973.robot;

import org.strongback.command.CommandGroup;
import org.strongback.drive.TankDrive;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class MiddleCubeNone extends CommandGroup {
	public MiddleCubeNone(TankDrive drive, GyroWrapper gyro) {
		sequentially(new TimedDriveCommand(drive, gyro, .3, false, 1500),
				new AngularTurnCommand(drive, gyro, .2, false, 90),
				new TimedDriveCommand(drive, gyro, .3, false, 1500),
				new AngularTurnCommand(drive, gyro, .2, false, -90),
				new TimedDriveCommand(drive, gyro, .3, false, 1000));
	}
}

