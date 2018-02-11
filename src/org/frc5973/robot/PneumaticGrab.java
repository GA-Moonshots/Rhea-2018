package org.frc5973.robot;

import org.strongback.command.Command;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class PneumaticGrab extends Command{

	DoubleSolenoid doubleSolenoid;
	public PneumaticGrab(DoubleSolenoid doubleSolenoid){
		this.doubleSolenoid = doubleSolenoid;
	}
	
	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		doubleSolenoid.set(DoubleSolenoid.Value.kForward);
		return false;
	}
	
	@Override
	public void interrupted() {

	}

	@Override
	public void end() {

	}

}
