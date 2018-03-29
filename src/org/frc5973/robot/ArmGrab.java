package org.frc5973.robot;

import org.strongback.command.Command;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class ArmGrab extends Command{
	boolean isCarrying;
	DoubleSolenoid doubleSolenoid;
	public ArmGrab(DoubleSolenoid doubleSolenoid){
		this.doubleSolenoid = doubleSolenoid;
	}
	
	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
			doubleSolenoid.set(DoubleSolenoid.Value.kForward);
		return true;
	}
	
	@Override
	public void interrupted() {

	}

	@Override
	public void end() {

	}

}
