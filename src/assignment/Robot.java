package assignment;

import java.awt.Point;
import java.io.Serializable;
import java.util.Queue;

public class Robot implements Serializable {
	private static final long serialVersionUID = 1L;
	private String robotName;
	private Point location;
	private int velocity;
	private String orientation;
	private String timeOfUpdate;
	private boolean alive;
	private boolean isInDanger = false;
	private int diameter;
	private boolean isClicked;
	private Queue<Point> lastPositions;

	public Robot(String name) {
		this.robotName = name;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public String getTimeOfUpdate() {
		return timeOfUpdate;
	}

	public void setTimeOfUpdate(String timeOfUpdate) {
		this.timeOfUpdate = timeOfUpdate;
	}

	public String getRobotName() {
		return robotName;
	}

	public void setRobotName(String robotName) {
		this.robotName = robotName;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public boolean isInDanger() {
		return isInDanger;
	}

	public void setInDanger(boolean isInDanger) {
		this.isInDanger = isInDanger;
	}

	public int getDiameter() {
		return diameter;
	}

	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}

	public boolean isClicked() {
		return isClicked;
	}

	public void setClicked(boolean isClicked) {
		this.isClicked = isClicked;
	}

	public Queue<Point> getLastPositions() {
		return lastPositions;
	}

	public void setLastPositions(Queue<Point> lastPositions) {
		this.lastPositions = lastPositions;
	}

}
