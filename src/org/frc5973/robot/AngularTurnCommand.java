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

/**
 * The command that drives the robot at a constant forward and turn speed for a
 * specific duration.
 */
public class AngularTurnCommand extends Command {

	private final TankDrive drive;
	private final double turnSpeed;
	private final boolean squareInputs;
	private GyroWrapper gyro;
	private double currentAngle;
	private double angle;

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
	public AngularTurnCommand(TankDrive drive, GyroWrapper gyro, double turnSpeed, boolean squareInputs,
			double angle) {
		super(drive);
		this.drive = drive;
		this.gyro = gyro;
		this.turnSpeed = turnSpeed;
		this.squareInputs = squareInputs;
		this.angle = angle;
	}

	@Override
	public boolean execute() {
		/*
		 * A loop that goes until the gyro reads an angle that 
		 * is +/- 3 of the desired angle
		 */
		System.out.print("startAng");
		currentAngle = gyro.getRawAngle();
		while ((currentAngle < (angle - 1)) || (currentAngle > (angle + 1))) {
			//checks to see whether should under correct
			if (currentAngle < (angle)) {
				drive.arcade(0, turnSpeed, squareInputs);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					System.out.println("Error here");
					e.printStackTrace();
				}
				drive.stop();
				currentAngle = gyro.getRawAngle();
				System.out.println(gyro.getRawAngle());

			}
			
			//checks to see whether should over correct
			if (currentAngle > (angle)) {
				drive.arcade(0, -.1, squareInputs);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					System.out.println("Error here");
					e.printStackTrace();
				}
				drive.stop();
				currentAngle = gyro.getRawAngle();
				System.out.println(gyro.getRawAngle());

			}
		}

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