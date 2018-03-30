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
import org.strongback.components.Motor;

/**
 * The command that drives the robot at a constant forward and turn speed for a
 * specific duration.
 */
public class ArmCommand extends Command {


	// Making this motor an instance variable so our autonomous mode can access this
	// as well
	private Motor lift_pulley;
	private Motor lift_elevator;

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
	public ArmCommand(Motor lift_pulley,
			Motor lift_elevator) {
		this.lift_pulley = lift_pulley;
		this.lift_elevator = lift_elevator;
	}

	@Override
	public boolean execute() {

		lift_pulley.setSpeed(-.85);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			System.out.println("Error here");
			e.printStackTrace();
		}
		lift_pulley.stop();			
		return true;

	}

	@Override
	public void interrupted() {
		System.out.println("Exeuction Interupted");
		lift_pulley.stop();
		lift_elevator.stop();
	}

	@Override
	public void end() {
		lift_pulley.stop();
		lift_elevator.stop();
	}

}