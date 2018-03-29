package org.frc5973.robot;

import org.strongback.command.Command;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class PneumaticGrab extends Command{
	boolean isCarrying;
	DoubleSolenoid doubleSolenoid;
	public PneumaticGrab(DoubleSolenoid doubleSolenoid){
		this.doubleSolenoid = doubleSolenoid;
	}
	
	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		if(!Robot.isCarryingGlobal){
			doubleSolenoid.set(DoubleSolenoid.Value.kForward);
			Robot.isCarryingGlobal = true;
			System.out.println("1");
		}
		
		else{
			doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
			Robot.isCarryingGlobal = false;
			System.out.println("2");

		}
		return true;
	}
	
	@Override
	public void interrupted() {

	}

	@Override
	public void end() {

	}

}
