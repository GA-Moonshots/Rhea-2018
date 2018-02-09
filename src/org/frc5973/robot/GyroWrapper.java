package org.frc5973.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class GyroWrapper extends ADXRS450_Gyro {
	public GyroWrapper() {
		super();
	}

	@Override
	public double getAngle() {
		double original = super.getAngle();
		if (original < 0) {
			return ((original % 360) + 360) % 360;
		} else {
			return original % 360;
		}
	}

}
