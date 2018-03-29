package org.frc5973.robot;

public class CustomRobotState {
	private int currentTiltTime;
	private int currentLiftTime;
	
	public CustomRobotState(int initialLiftTime, int initialTiltTime) {
		currentTiltTime = initialTiltTime;
		currentLiftTime = initialLiftTime;
	}
	
	public void setCurrentTiltTime(int newTiltTime) {
		currentTiltTime = newTiltTime;
	}
	public int getCurrentTiltTime() {
		return currentTiltTime;
	}
	public void setCurrentLiftTime(int newLiftTime) {
		currentLiftTime = newLiftTime;
	}
	public int getCurrentLiftTime() {
		return currentLiftTime;
	}
	
}
