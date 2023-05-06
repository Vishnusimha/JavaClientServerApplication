package assignment;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Queue;
import java.util.Set;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class MyServerCanvas extends Canvas implements MouseListener {

	private MyCanvasCallback canvasCallback;
	private Image roboImg;
	private Color drawColor = Color.GRAY;
	private HashMap<String, Robot> map = new HashMap<String, Robot>();
	private int collisionSafteyMargin;
	private boolean toggle;

	public MyServerCanvas(MyCanvasCallback app) {
		this.canvasCallback = app;
		this.setPreferredSize(new Dimension(600, 600));
		this.setMaximumSize(new Dimension(600, 600));
		this.setSize(getPreferredSize());
		this.setBackground(Color.BLACK);
		this.addMouseListener(this);
		this.repaint();
	}

	public void paint(Graphics g) {
		g.setColor(drawColor);
//		Design setup
		int noOfrows = 6;
		int noOfcolumns = 6;
		int width = getSize().width;
		int height = getSize().height;
		roboImg = new ImageIcon("src/robo.png").getImage();

		// Columns
		int rowWidth = width / (noOfcolumns);
		for (int i = 0; i < noOfcolumns; i++) {
			g.drawLine(i * rowWidth, 0, i * rowWidth, height);
			g.drawString("" + i * 100, i * rowWidth, 10);
		}

		// Rows
		int rowHeight = height / (noOfrows);
		for (int i = 0; i < noOfrows; i++) {
			g.drawLine(0, i * rowHeight, width, i * rowHeight);
			g.drawString("" + i * 100, 10, i * rowHeight);
		}

		g.setColor(Color.WHITE);

		System.out.println("No of robots = " + map.size());
		this.canvasCallback.setClientsCount(map.size());

		for (Robot r : map.values()) {
			Point location = r.getLocation();
			if (r.isInDanger()) {
				System.out.println("COLLISION ALERTING TO === " + r.getRobotName() + map.size());
				map.get(r.getRobotName()).setInDanger(false);
				g.setColor(Color.RED);
				g.drawOval(location.x, location.y, 60, 60);
			}
			if (r.isClicked()) {
				g.setColor(Color.GREEN);
				g.drawString(r.getRobotName() + " Dim = " + r.getDiameter(), location.x, location.y - 50);
			}
			g.setColor(Color.GREEN);
			g.drawString("V = " + r.getVelocity(), location.x, location.y - 30);
			g.drawString("O = " + r.getOrientation(), location.x, location.y - 15);
			g.drawString("X = " + location.x + " Y = " + location.y, location.x, location.y);
			g.fillOval(location.x + 12, location.y + 20, 10, 10);
			g.drawImage(roboImg, location.x, location.y, this);

			if (toggle) {
				Object[] pos = r.getLastPositions().toArray();
				Point a = (Point) pos[0];
				Point b = (Point) pos[1];
				Point c = (Point) pos[2];

				g.setColor(Color.ORANGE);
				g.drawString("X = " + c.x + " Y = " + c.y, location.x, location.y + 60);
				g.setColor(Color.CYAN);
				g.drawString("X = " + b.x + " Y = " + b.y, location.x, location.y + 80);
				if (pos.length > 3) {
					g.setColor(Color.MAGENTA);
					g.drawString("X = " + a.x + " Y = " + a.y, location.x, location.y + 100);
				}
			}
		}
	}

	protected void setColor(Color c) {
		drawColor = c;
		this.repaint();
	}

	protected void setCollisionSafteyMargin(int collisionSafteyMargin) {
		this.collisionSafteyMargin = collisionSafteyMargin;
	}

	protected void setToggle(boolean toggle) {
		this.toggle = toggle;
		this.repaint();
	}

	protected void setRobot(Robot robot) {
		if (robot.isAlive()) {
			if (!map.containsKey(robot.getRobotName()) && !map.containsValue(robot)) {
				System.out.println("MyServerCanvas: NEW ROBOT ADDED");
				map.put(robot.getRobotName(), robot);
				storePosition(robot);
			} else {
				storePosition(robot);
				System.out.println("MyServerCanvas: UPDATING VALUES");
				map.get(robot.getRobotName()).setLocation(robot.getLocation());
				map.get(robot.getRobotName()).setVelocity(robot.getVelocity());
				map.get(robot.getRobotName()).setOrientation(robot.getOrientation());
			}
		} else {
			removeRobot(robot);
		}
		canvasCallback.setClientsCount(map.size());
		checkCollision();
		this.repaint();
	}

	private void storePosition(Robot robot) {
		Queue<Point> lastPositions = map.get(robot.getRobotName()).getLastPositions();

		if (lastPositions.size() < 4) {
			System.out.println("IFFFFFFF");
			lastPositions.add(robot.getLocation());
		} else {
			System.out.println("ELSE");
			lastPositions.poll();
			lastPositions.add(robot.getLocation());
		}
		robot.setLastPositions(lastPositions);
		map.get(robot.getRobotName()).setLastPositions(lastPositions);
	}

	private void checkCollision() {
		int safteyMargin = 5;
		if (collisionSafteyMargin > 5 && collisionSafteyMargin < 15) {
			safteyMargin = collisionSafteyMargin;
		}
		System.out.println(safteyMargin);

		for (Robot r : map.values()) {
			Point location1 = r.getLocation();
			int radius1 = r.getDiameter() / 2;
			
			for (Robot rr : map.values()) {
				Point location2 = rr.getLocation();
				int radius2 = rr.getDiameter() / 2;
				if (!r.equals(rr)) {
					double distance = Point.distance(location1.x, location1.y, location2.x, location2.y);
					System.out.println("DISTANCE = " + distance);
					System.out.println("R1 = " + radius1 + " R2 = " + radius2);
					//((safteyMargin * 2) because it applies for both robots in check
					if (distance < ((safteyMargin * 2) + radius1 + radius2)) {
						map.get(r.getRobotName()).setInDanger(true);
						map.get(rr.getRobotName()).setInDanger(true);
						System.out.println(r.getRobotName() + " " + rr.getRobotName() + " ARE IN DANGER");
					}
				}
			}
		}
	}

	private void removeRobot(Robot robot) {
		System.out.println("REMOVE ROBOT CALLED");
		map.remove(robot.getRobotName());
	}

	protected Set<String> getRobots() {
		return map.keySet();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point p = new Point(e.getX(), e.getY());

		for (Robot r : map.values()) {
			Point rLocation = r.getLocation();
			double distance = Point.distance(p.x, p.y, rLocation.x, rLocation.y);
			if (distance < 50) {
				if (!r.isClicked()) {
					r.setClicked(true);
				} else {
					r.setClicked(false);
				}
			}
		}

		this.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
