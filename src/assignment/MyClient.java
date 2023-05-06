package assignment;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

public class MyClient extends Thread implements ActionListener, WindowListener {
	private JFrame clientFrame;
	private JPanel infoPanel, buttonsPanel;
	private JButton up, down, left, right, frequentUpdate;
	private JSlider speedControl;
	private JLabel currentConnectionStatus, lastMessageSentTime;
	private Robot robot;
	private MyDateTimeService theDateService = new MyDateTimeService();
	private boolean isConnected = false;
	private boolean running = true, paused = false;
	private Thread thread;

	private static int portNumber = 5050;
	private Socket socket = null;
	private ObjectOutputStream os = null;
	private ObjectInputStream is = null;

	public MyClient(String[] args) {
		this.thread = new Thread(this);

		clientFrame = new JFrame();
		clientFrame.setTitle("Client " + args[1]);
		clientFrame.setSize(600, 600);
		clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientFrame.addWindowListener(this);
		clientFrame.setLayout(new GridLayout(1, 1)); // layout

		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(0, 4));

		up = new JButton("Up");
		up.setToolTipText("Robot move upwards");
		down = new JButton("Down");
		down.setToolTipText("Robot move downwards");
		left = new JButton("Left");
		left.setToolTipText("Robot move left side");
		right = new JButton("Right");
		right.setToolTipText("Robot move right side");
		frequentUpdate = new JButton("Switch off Automatic Updates");
		frequentUpdate.setToolTipText("By clicking this you can send updates to server for every 10 seconds");
		frequentUpdate.setBackground(Color.GREEN);
		frequentUpdate.setForeground(Color.BLACK);

		buttonsPanel.add(left);
		buttonsPanel.add(up);
		buttonsPanel.add(down);
		buttonsPanel.add(right);

		up.addActionListener(this);
		down.addActionListener(this);
		left.addActionListener(this);
		right.addActionListener(this);
		frequentUpdate.addActionListener(this);

		speedControl = new JSlider(1, 6, 1);
		speedControl.putClientProperty("JSlider.isFilled", Boolean.TRUE);
		speedControl.setPaintTicks(true);
		speedControl.setMajorTickSpacing(2);
		speedControl.setMinorTickSpacing(1);
		speedControl.setPaintLabels(true);
		speedControl.setSnapToTicks(true);
		speedControl.setLabelTable(speedControl.getLabelTable());
		speedControl.getAccessibleContext().setAccessibleName("Minor Ticks");
		speedControl.setBackground(Color.BLACK);
		speedControl.setForeground(Color.WHITE);

		infoPanel = new JPanel();
		TitledBorder border = new TitledBorder("Information Panel");

		border.setTitleColor(Color.WHITE);
		infoPanel.setBackground(Color.BLACK);
		infoPanel.setBorder(border);

		JPanel sliderPanel = new JPanel();
		TitledBorder sppedSelection = new TitledBorder("Select Speed");
		sppedSelection.setTitleColor(Color.WHITE);
		sliderPanel.setBackground(Color.BLACK);
		sliderPanel.setBorder(sppedSelection);
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
		sliderPanel.add(speedControl);

		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setAlignmentY(portNumber);
		currentConnectionStatus = new JLabel();
		lastMessageSentTime = new JLabel();

		currentConnectionStatus.setText("Connection Status = ?");
		lastMessageSentTime.setText("last Message Sent Time = ?");

		currentConnectionStatus.setForeground(Color.WHITE);
		lastMessageSentTime.setForeground(Color.WHITE);

		infoPanel.add(frequentUpdate);
		infoPanel.add(sliderPanel);
		infoPanel.add(currentConnectionStatus);
		infoPanel.add(lastMessageSentTime);

		clientFrame.add(infoPanel);
		clientFrame.add(buttonsPanel);

		clientFrame.setVisible(true);

//		client 
		if (!connectToServer(args[0])) {
			System.out.println("XX. Failed to open socket connection to: " + args[0]);
		}
	}

	public static void main(String args[]) {
		System.out.println("**. Java Client Application - EE402 OOP Module, DCU");
		if (args.length == 3) {
			MyClient myClientObj = new MyClient(args);
			myClientObj.createRobotObect(args);
		} else {
			System.out.println("Error: you must provide the address of the server");
			System.out.println("Usage is:  java Client x.x.x.x  (e.g. java Client 192.168.7.2)");
			System.out.println("      or:  java Client hostname (e.g. java Client localhost)");
		}

		System.out.println("**. End of Clinet Main method Application.");
	}

	private boolean connectToServer(String serverIP) {
		try { // open a new socket to the server
			this.socket = new Socket(serverIP, portNumber);
			this.os = new ObjectOutputStream(this.socket.getOutputStream());
			this.is = new ObjectInputStream(this.socket.getInputStream());
			System.out.println("00. -> Connected to Server:" + this.socket.getInetAddress() + " on port: "
					+ this.socket.getPort());
			System.out.println("    -> from local address: " + this.socket.getLocalAddress() + " and port: "
					+ this.socket.getLocalPort());

			this.currentConnectionStatus.setForeground(Color.GREEN);
			this.currentConnectionStatus.setText("Connected with Server");
			setConnected(true);
			System.out.println("STARTING THREAD");
		} catch (Exception e) {
			this.running = false;
			setConnected(false);
			this.currentConnectionStatus.setForeground(Color.RED);
			this.currentConnectionStatus.setText("Connection Status: No Connection");
			System.out.println("XX. Failed to Connect to the Server at port: " + portNumber);
			System.out.println("    Exception: " + e.toString());
			return false;
		}
		return true;
	}

	private void createRobotObect(String[] args) {
		Queue<Point> lastPositions = new LinkedList<Point>();

		this.robot = new Robot(args[1]);
		robot.setVelocity(this.speedControl.getValue());
		robot.setAlive(true);
		robot.setLocation(new Point(20, 200));
		robot.setDiameter(Integer.parseInt(args[2]));
		lastPositions.add(robot.getLocation());
		robot.setLastPositions(lastPositions);

		this.lastMessageSentTime.setText("Last Message Sent : " + theDateService.getDateAndTime());
		System.out.println("createRobotObect Name: " + robot.getRobotName() + " location: " + robot.getLocation()
				+ "velocity: " + robot.getVelocity());
		sendUpdateToServer();
		this.thread.start();
	}

	private void updatePositionAndVelocityValues(Point p, String orientation) {
		this.robot.setOrientation(orientation);
		this.robot.setLocation(p);
		this.robot.setVelocity(this.speedControl.getValue());
		this.lastMessageSentTime.setText("Last Message Sent : " + theDateService.getDateAndTime());
		System.out.println("Name: " + robot.getRobotName() + " location: " + robot.getLocation() + "velocity: "
				+ robot.getVelocity());
		sendUpdateToServer();
	}

	private void sendUpdateToServer() {
		System.out.println(isConnected());
		if (isConnected()) {
			System.out.println(
					"Updating to server: ori : " + robot.getOrientation() + robot.getRobotName() + robot.getLocation());
			this.lastMessageSentTime.setText("Last Message Sent : " + theDateService.getDateAndTime());
			this.send(robot);
		}
	}

	// method to send a generic object.
	private void send(Object o) {
		try {
			System.out.println("02. -> Sending an object...");
			if (o.equals(robot)) {
				System.out.println("CORRECT OBJ " + robot.getRobotName() + " " + robot.getOrientation() + "Alive ? = "
						+ robot.isAlive());
			}
			os.reset();
			os.writeObject(o);
			os.flush();
		} catch (Exception e) {
			setConnected(false);
			this.currentConnectionStatus.setForeground(Color.RED);
			this.currentConnectionStatus.setText("Connection Status: No Connection");
			System.out.println("XX. Exception Occurred on Sending:" + e.toString());
		}
	}

	public void toggleCount() {
		synchronized (this) {
			this.paused = !this.paused;
			if (!this.paused)
				this.notify();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int speed = this.speedControl.getValue();
		if (robot != null) {
			switch (e.getActionCommand()) {
			case "Up":
				System.out.println("position x " + Math.max(0, robot.getLocation().x) + "y "
						+ Math.max(0, robot.getLocation().y - 10) + speed);

				this.updatePositionAndVelocityValues(
						new Point(Math.max(0, robot.getLocation().x), Math.max(0, robot.getLocation().y - 10) - speed),
						"North");
				break;
			case "Down":
				System.out.println("position x " + Math.max(0, robot.getLocation().x) + "y "
						+ Math.max(0, robot.getLocation().y + 10));

				this.updatePositionAndVelocityValues(new Point(Math.max(0, robot.getLocation().x),
						Math.min(550, robot.getLocation().y + 10) + speed), "South");
				break;
			case "Left":
				System.out.println("position x " + Math.max(0, robot.getLocation().x - 10) + "y "
						+ Math.max(0, robot.getLocation().y));

				this.updatePositionAndVelocityValues(
						new Point(Math.max(0, robot.getLocation().x - 10) - speed, Math.max(0, robot.getLocation().y)),
						"West");
				break;
			case "Right":
				System.out.println("position x " + Math.max(0, robot.getLocation().x + 10) + "y "
						+ Math.max(0, robot.getLocation().y));
				this.updatePositionAndVelocityValues(new Point(Math.min(550, robot.getLocation().x + 10) + speed,
						Math.max(0, robot.getLocation().y)), "East");
				break;
			case "Switch off Automatic Updates":
				System.out.println("Switched off Automatic Update");
				this.running = false;
				break;
			default:
				System.out.println("Default");
			}
		}

	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("WINDOW CLOSING");
		if (this.robot != null) {
			robot.setAlive(false);
			this.send(robot);
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public void run() {
		while (running) {
			sendUpdateToServer();
			System.out.println(
					"Robot Info" + robot.getRobotName() + " " + robot.getLocation() + " " + robot.getVelocity());
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				System.out.println("Thread was Interrupted!");
			}
		}
	}
}
