# Documentation:

The classes and interfaces present in this project are
* MyServer
* MyServerCanvas
* MyClient
* MyCanvasCallback - Interface
* MyThreadedConnectionHandler
* Robot
* MyDateTimeService

## How to run Applications ?

The Server can be started by running it from eclipse. No command line args are required.
The client needs to be started by passing the command line arguments in the below format.
When running from eclipse: localhost RobotName Dimension

**When running from a command prompt:**

**java assignment.MyClient** localhost RobotName Dimension

**Example:** java assignment.MyClient localhost SteelRobot 20
Note: Dimension is Integer; it initially takes as a string from command line args and parses it to Integer.
A robot creates on the server screen as soon as we run the client application.

## Tests Performed:
1. Started the server, connected with 3 robots at a time, and moved the robots on the server canvas GUI. All are moving perfectly.
2. When speed changes from the client GUI slider, robots adapt and move accordingly.
3. The connection status of the client with the Server is properly displayed on the client GUI. When the client connection with the Server is successful, Connected with Server in green color is displayed on the GUI. Else for any failure or no connection, it shows Connection Status: No Connection in red.
4. The 10 seconds frequent update thread is stopped by clicking on the Switch off Automatic Updates button, and it's working as expected.
5. The last Message sent from the client is observed, and it's working as expected within exact seconds.
6. A collision check is performed by connecting 4 client robots. When any robot goes close to another robot, a red circle is drawn on the robots which are colliding/ in danger.
7. The last 3 positions check is performed by clicking on the Previous positions Toggle button on the Server GUI. When it is clicked, the last 3 positions of each robot are shown under the robot in different colors.
8. A mouse click test is performed to see the Robot Name on top of robots and names displayed as expected. Once clicked on the same robot, it is removed.
9. Position, Orientation, and velocity displayed on the server canvas are checked by moving the robot in different directions with different speed settings on the client GUI.