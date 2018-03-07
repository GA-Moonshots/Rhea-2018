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
public class ArmReturn extends Command {

	private DoubleSolenoid exDub;
	private Compressor c;
	
	//Making this motor an instance variable so our autonomous mode can access this as well
	private Motor lift_pulley;
	private Motor lift_elevator;
	private double lift_speed;
	private long lift_time;
	private double tilt_speed;
	private long tilt_time;
	private boolean close_grip;

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
	public ArmReturn(Motor lift_pulley, Motor lift_elevator, DoubleSolenoid exDub, Compressor c,
			double lift_speed, long lift_time, double tilt_speed, long tilt_time, boolean close_grip) {
		this.lift_pulley = lift_pulley;
		this.lift_elevator = lift_elevator;
		this.exDub = exDub;
		this.c = c;
		this.lift_speed = lift_speed;
		this.lift_time = lift_time;
		this.tilt_speed = tilt_speed;
		this.tilt_time = tilt_time;
		this.close_grip = close_grip;
	}

	@Override
	public boolean execute() {
		if(Robot.currentState.equals("mid")){
			ArmCommand returnFromMid = new ArmCommand(lift_pulley, lift_elevator, exDub, c, -.3, 1000, 0, 0, false);
			Strongback.submit(returnFromMid);		
			return true;
		}
		else if (Robot.currentState.equals("high")){
			ArmCommand returnFromHigh = new ArmCommand(lift_pulley, lift_elevator, exDub, c, -.3, 2000, -.3, 500, false);
			Strongback.submit(returnFromHigh);		
			return true;
		}
		return true;
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