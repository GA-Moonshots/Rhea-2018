package org.frc5973.robot;

import org.strongback.command.Command;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class PneumaticRelease extends Command{

	DoubleSolenoid doubleSolenoid;
	public PneumaticRelease(DoubleSolenoid doubleSolenoid){
		this.doubleSolenoid = doubleSolenoid;
	}
	
	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
		return false;
	}
	
	@Override
	public void interrupted() {

	}

	@Override
	public void end() {

	}

}
