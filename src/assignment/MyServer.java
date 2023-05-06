package assignment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class MyServer implements ActionListener, MyCanvasCallback {

	private JFrame frame;
	private static MyServerCanvas canvas;
	private JPanel leftPanel, rightPanel;
	private JLabel instruction, connectionStatus, noOfClientsConnected, robotsInDanger;
	private JTextField collisionSafteyMargin;
	private JToggleButton toggleButton;

	private static int portNumber = 5050;

	public MyServer() {
//		Frame setup
		frame = new JFrame();
		frame.setTitle("Server");
		frame.setSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(1, 1));

//		left panel
		leftPanel = new JPanel();
		leftPanel.setLayout(new FlowLayout());
		leftPanel.setSize(new Dimension(200, 600));
		leftPanel.setBackground(Color.BLACK);
//		setup canvas
		canvas = new MyServerCanvas(this);
		leftPanel.add(canvas);

//		panel setup
		rightPanel = new JPanel();
		rightPanel.setBackground(Color.WHITE);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setSize(new Dimension(400, 400));

		collisionSafteyMargin = new JTextField();
		collisionSafteyMargin.setMaximumSize(new Dimension(400, 20));

		instruction = new JLabel();
		instruction.setForeground(Color.GRAY);
		connectionStatus = new JLabel();
		noOfClientsConnected = new JLabel();
		robotsInDanger = new JLabel();
		robotsInDanger.setForeground(Color.RED);
		toggleButton = new JToggleButton("Previous Positions Toggle");

//		listeners
		toggleButton.addActionListener(this);
		collisionSafteyMargin.addActionListener(this);

// 		Setting text to text Labels
		instruction.setText("Enter Collision Margin for Robots below from range 5 to 15");
		connectionStatus.setText("connectionStatus");
		noOfClientsConnected.setText("noOfClientsConnected");
//		Adding components to Panel
		rightPanel.add(connectionStatus);
		rightPanel.add(noOfClientsConnected);
		rightPanel.add(instruction);
		rightPanel.add(collisionSafteyMargin);
		rightPanel.add(toggleButton);

		rightPanel.add(robotsInDanger);

//		Adding to frame
		frame.add(canvas);
		frame.add(rightPanel);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new MyServer();
//		Threaded server
		boolean listening = true;
		ServerSocket serverSocket = null;
		// Set up the Server Socket
		try {
			serverSocket = new ServerSocket(portNumber);
			System.out.println("New Server has started listening on port: " + portNumber);
		} catch (IOException e) {
			System.out.println("Cannot listen on port: " + portNumber + ", Exception: " + e);
			System.exit(1);
		}

		while (listening) {
			Socket clientSocket = null;
			try {
				System.out.println("**. Listening for a connection...");
				clientSocket = serverSocket.accept();
				System.out.println("00. <- Accepted socket connection from a client: ");
				System.out.println("    <- with address: " + clientSocket.getInetAddress().toString());
				System.out.println("    <- and port number: " + clientSocket.getPort());
			} catch (IOException e) {
				System.out.println("XX. Accept failed: " + portNumber + e);
				listening = false;
			}
			MyThreadedConnectionHandler con = new MyThreadedConnectionHandler(clientSocket, canvas);
			con.start();
			System.out.println("02. -- Finished communicating with client:" + clientSocket.getInetAddress().toString());
		}
		try {

			System.out.println("04. -- Closing down the server socket gracefully.");
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("XX. Could not close server socket. " + e.getMessage());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("ServerGUI: actionPerformed" + e.getActionCommand());
		if (e.getSource().equals(collisionSafteyMargin)) {
			canvas.setCollisionSafteyMargin(Integer.parseInt(e.getActionCommand()));
		}

		if (e.getSource().equals(toggleButton)) {
			canvas.setToggle(toggleButton.isSelected());
		}
	}

	@Override
	public void setClientsCount(int count) {
		if (count > 0) {
			connectionStatus.setForeground(Color.green);
			connectionStatus.setText("Connected to Clients");
		} else {
			connectionStatus.setForeground(Color.RED);
			connectionStatus.setText("No Connection with Clients");
		}
		noOfClientsConnected.setText("No of Clients Connected : " + count);
	}
}
