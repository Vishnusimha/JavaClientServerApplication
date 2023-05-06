# Documentation:

## Design :

This Client/Server Application is designed with a step-by-step implementation approach on top of the provided template. The Client GUI layout is designed in a user-friendly way to move around the buttons with ease to move the robot quickly. The class structure, its features, and client/server communication mechanisms are explained below.
The classes and interfaces present in this project are
* MyServer
* MyServerCanvas
* MyClient
* MyCanvasCallback - Interface
* MyThreadedConnectionHandler
* Robot
* MyDateTimeService
* 
**MyServer** class has the base network connection code given as in the template and with added GUI that I developed with **Java SWING** and **Canvas** in **MyServerCanvas** class. This server is capable of handling more than 3 robots at the same time.
**MyServerCanvas** is a **Graphical GUI Java AWT Canvas** it's used to draw the graphical grid, robots, and other information. This class presents all the drawings and supporting business logic and states.
The Server GUI contains left and right panels. 

The left side panel contains a canvas grid to show the robots' movements. On the right panel, I added some swing components to show information like No of clients connected, Connection status with clients, and a toggle button to show each robot client's past 3 positions information. Finally, a text field to set the collision margin for robots.
We can enter the collision margin from the server GUI, and the previous positions toggle button can be clicked to see the last 3 positions of each robot on the canvas.

**MyClient** class has the base network connection code given in the template and added business logic to create a robot object and send it to the server along with the positions and data updating logic.
Buttons in the client GUI are provided with an additional tooltip feature which shows text on the tooltip when we place the cursor on it. An additional opt-out button is added to the client to switch 10-second automatic updates to the server. Furthermore, users can select the robot's speed from the slider developed by the swing **JSlider**.
**MyCanvasCallback** is an interface that can set a number of clients connected from **MyServerCanvas** and receive on the Server class.

**MyThreadedConnectionHandler** is mostly the same as the template. However, it receives a robot object from the network stream and sends it to the MyServerCanvas class, where it handles business logic is present.
A robot class is created to add the required data to the robot object and transfer over the network from Clients to server. Only robot objects are used for network communication in this project.
MyDateTimeService is used to get the date and time from the Calendar instance and to update the last message sent to the client.

Client/Server communications happen between Client and Server classes by exchanging the data with the Robot class. All the required data is set to the Robot object and sent over the network. It is then received by the server's threaded connection handler and provides information to **MyServerCanvas** it then displays data on the GUI.


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