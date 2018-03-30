/*
 * Strongback
 * Copyright 2015, Strongback and individual contributors by the @authors tag.
 * See the COPYRIGHT.txt in the distribution for a full listing of individual
 * contributors.
 *
 * Licensed under the MIT License; you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://opensource.org/licenses/MIT
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.frc5973.robot;

import org.strongback.command.Command;
import org.strongback.drive.TankDrive;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

/**
 * The command that drives the robot at a constant forward and turn speed for a
 * specific duration.
 */
public class TimedDriveCommand extends Command {

	private final TankDrive drive;
	private final double driveSpeed;
	private final boolean squareInputs;
	private long time_move;
	private long totalChecks;
	private long currentCheck;
	private GyroWrapper gyro;

	/**
	 * Create a new autonomous command.
	 * 
	 * @param drive
	 *            the chassis
	 * @param driveSpeed
	 *            the speed at which to drive forward; should be [-1.0, 1.0]
	 * @param turnSpeed
	 *            the speed at which to turn; should be [-1.0, 1.0]
	 * @param squareInputs
	 *            whether to increase sensitivity at low speeds
	 * @param duration
	 *            the duration of this command; should be positive
	 */
	public TimedDriveCommand(TankDrive drive, GyroWrapper gyro, double driveSpeed, boolean squareInputs,
			long time_move) {
		super(drive);
		this.drive = drive;
		this.driveSpeed = -driveSpeed;
		this.squareInputs = squareInputs;
		this.time_move = time_move;
		this.totalChecks = 20 * time_move/1000;
		this.currentCheck = 0;
		this.gyro = gyro;
	}

	@Override
	public boolean execute() {
		gyro.reset();
		while (currentCheck < totalChecks) {
			drive.arcade(-driveSpeed, -gyro.getRawAngle()*.03, squareInputs);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				System.out.println("Error here");
				e.printStackTrace();
			}
			drive.stop();
			currentCheck++;	
			System.out.println(gyro.getRawAngle());
		}
		currentCheck = 0;

		return true;
	}

	@Override
	public void interrupted() {
		drive.stop();
	}

	@Override
	public void end() {
		drive.stop();
	}

}