package org.frc5973.robot;

import org.strongback.Strongback;
import org.strongback.command.CommandGroup;
import org.strongback.components.Motor;
import org.strongback.drive.TankDrive;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;

public class RightDrop extends CommandGroup {
	private GameDataState data;
	public RightDrop(String data, Motor lift_pulley, Motor life_elevator, TankDrive drive, GyroWrapper gyro, DoubleSolenoid exDub) {
		// drop right
		if (data.length() > 0) {
			if (data.charAt(0) == 'R') {
				sequentially(new RightCubeRight(drive, gyro), new ArmCommand(lift_pulley, life_elevator),
						new ArmRelease(exDub));
			} else if (data.charAt(0) == 'L') {
				sequentially(new RightCubeLeft(drive, gyro), new ArmCommand(lift_pulley, life_elevator),
						new ArmRelease(exDub));
			}
		} else {
			sequentially(new RightCubeNone(drive, gyro));
		}
		System.out.println("Right is Created");
		
	}
	
	public void setData(String newData) {
		data.setGameData(newData);
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
