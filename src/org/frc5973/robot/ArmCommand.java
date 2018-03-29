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

import org.strongback.Strongback;
import org.strongback.command.Command;
import org.strongback.components.Motor;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * The command that drives the robot at a constant forward and turn speed for a
 * specific duration.
 */
public class ArmCommand extends Command {


	// Making this motor an instance variable so our autonomous mode can access this
	// as well
	private Motor lift_pulley;
	private Motor lift_elevator;

	private String requestedState;
	private int currentTiltTime;
	private int currentLiftTime;
	private int tiltTime;
	private int liftTime;
	private int liftScalar;
	private int tiltScalar;
	private RobotState robotState;
	private final double tiltSpeed = .3;
	private final double liftSpeed = .3;

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
	public ArmCommand(RobotState robotState, String requestedState, Motor lift_pulley,
			Motor lift_elevator) {
		this.lift_pulley = lift_pulley;
		this.lift_elevator = lift_elevator;
		this.currentTiltTime = robotState.getCurrentTiltTime();
		this.currentLiftTime = robotState.getCurrentLiftTime();
		this.requestedState = requestedState;
		liftScalar = 1;
		tiltScalar = 1;
		this.robotState = robotState;
	}

	@Override
	public boolean execute() {
		if (requestedState.equals("low")) {
			liftTime = 0 - currentLiftTime;
			if (liftTime < 0) {
				liftScalar = -1;
			}

			tiltTime = 0 - currentTiltTime;
			if (tiltTime < 0) {
				tiltScalar = -1;
			}

		} else if (requestedState.equals("mid")) {
			liftTime = 1000 - currentLiftTime;
			if (liftTime < 0) {
				liftScalar = -1;
			}

			tiltTime = 1000 - currentTiltTime;
			if (tiltTime < 0) {
				tiltScalar = -1;
			}
		}

		else if (requestedState.equals("high")) {
			liftTime = 2000 - currentLiftTime;
			if (liftTime < 0) {
				liftScalar = -1;
			}

			tiltTime = 2000 - currentTiltTime;
			if (tiltTime < 0) {
				tiltScalar = -1;
			}
		}

		else if (requestedState.equals("max")) {
			liftTime = 3000 - currentLiftTime;
			if (liftTime < 0) {
				liftScalar = -1;
			}

			tiltTime = 3000 - currentTiltTime;
			if (tiltTime < 0) {
				tiltScalar = -1;
			}
		}

		lift_elevator.setSpeed(liftScalar*liftSpeed);
		try {
			Thread.sleep(liftTime);
		} catch (InterruptedException e) {
			System.out.println("Error here");
			e.printStackTrace();
		}
		lift_elevator.stop();

		lift_pulley.setSpeed(tiltScalar*tiltSpeed);
		try {
			Thread.sleep(tiltTime);
		} catch (InterruptedException e) {
			System.out.println("Error here");
			e.printStackTrace();
		}
		lift_pulley.stop();

		robotState.setCurrentLiftTime(liftTime);
		robotState.setCurrentTiltTime(tiltTime);
		
		return true;
		// if(Robot.currentState.equals("low")) {
		// if (lift_time > 0){
		// lift_elevator.setSpeed(lift_speed);
		// try {
		// Thread.sleep(lift_time);
		// } catch (InterruptedException e) {
		// System.out.println("Error here");
		// e.printStackTrace();
		// }
		// lift_elevator.stop();
		// }
		//
		// if (tilt_time > 0){
		// lift_pulley.setSpeed(tilt_speed);
		// try {
		// Thread.sleep(tilt_time);
		// } catch (InterruptedException e) {
		// System.out.println("Error here");
		// e.printStackTrace();
		// }
		// lift_pulley.stop();
		// }
		//
		// return true;
		//
		// }
		//
		// else {
		// Strongback.submit(new ArmReturn(lift_pulley, lift_elevator, exDub, c, 0, 0,
		// 0, 0, false));
		//
		// if (lift_time > 0){
		// lift_elevator.setSpeed(lift_speed);
		// try {
		// Thread.sleep(lift_time);
		// } catch (InterruptedException e) {
		// System.out.println("Error here");
		// e.printStackTrace();
		// }
		// lift_elevator.stop();
		// }
		//
		// if (tilt_time > 0){
		// lift_pulley.setSpeed(tilt_speed);
		// try {
		// Thread.sleep(tilt_time);
		// } catch (InterruptedException e) {
		// System.out.println("Error here");
		// e.printStackTrace();
		// }
		// lift_pulley.stop();
		// }
		//
		// return true;
		// }

	}

	@Override
	public void interrupted() {
		lift_pulley.stop();
		lift_elevator.stop();
	}

	@Override
	public void end() {
		lift_pulley.stop();
		lift_elevator.stop();
	}

}