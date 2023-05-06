package assignment;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MyThreadedConnectionHandler extends Thread {
	private Socket clientSocket = null; // Client socket object
	private ObjectInputStream is = null; // Input stream
	private ObjectOutputStream os = null; // Output stream
	private MyServerCanvas canvas;

	public MyThreadedConnectionHandler(Socket clientSocket, MyServerCanvas canvas) {
		this.clientSocket = clientSocket;
		this.canvas = canvas;
	}

	public void run() {
		try {
			this.is = new ObjectInputStream(clientSocket.getInputStream());
			this.os = new ObjectOutputStream(clientSocket.getOutputStream());
			while (this.readCommand()) {
				System.out.println("Handler Reading commands...");
			}
		} catch (IOException e) {
			System.out.println("XX. There was a problem with the Input/Output Communication:");
			e.printStackTrace();
		}
	}

	private boolean readCommand() {
		Robot robot = null;
		try {
			robot = (Robot) is.readObject();
		} catch (Exception e) { // catch a general exception
			this.closeSocket();
			return false;
		}

		if (robot.getClass() == Robot.class) {
			canvas.setRobot(robot);
		} else {
			this.sendError("Invalid robo: " + robot.getRobotName());
		}

		return true;
	}

	private void send(Object o) {
		try {
			System.out.println("02. -> Sending (" + o + ") to the client.");
			this.os.writeObject(o);
			this.os.flush();
		} catch (Exception e) {
			System.out.println("XX." + e.getStackTrace());
		}
	}

	public void sendError(String message) {
		this.send("Error:" + message); 
	}

	public void closeSocket() { 
		try {
			this.os.close();
			this.is.close();
			this.clientSocket.close();
		} catch (Exception e) {
			System.out.println("XX. " + e.getStackTrace());
		}
	}

}