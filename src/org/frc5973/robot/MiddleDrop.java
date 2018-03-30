package org.frc5973.robot;

import org.strongback.Strongback;
import org.strongback.command.CommandGroup;
import org.strongback.components.Motor;
import org.strongback.drive.TankDrive;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;

public class MiddleDrop extends CommandGroup {
	public MiddleDrop(Motor lift_pulley, Motor life_elevator, TankDrive drive, GyroWrapper gyro, DoubleSolenoid exDub) {
		// drop right
		if (Robot.gameData.length() > 0) {
			if (Robot.gameData.charAt(0) == 'R') {
				sequentially(new MiddleCubeRight(drive, gyro), new ArmCommand(lift_pulley, life_elevator),
						new ArmRelease(exDub));
			} else if (Robot.gameData.charAt(0) == 'L') {
				sequentially(new MiddleCubeLeft(drive, gyro), new ArmCommand(lift_pulley, life_elevator),
						new ArmRelease(exDub));
			}
		} else {
			sequentially(new MiddleCubeNone(drive, gyro));
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
