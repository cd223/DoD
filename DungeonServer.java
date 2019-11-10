/**
 *   _____                                                  __   _____                              
 *	|  __ \                                                / _| |  __ \                             
 *	| |  | |_   _ _ __   __ _  ___  ___  _ __  ___    ___ | |_  | |  | | ___   ___   ___  _ __ ___  
 *	| |  | | | | | '_ \ / _` |/ _ \/ _ \| '_ \/ __|  / _ \|  _| | |  | |/ _ \ / _ \ / _ \| '_ ` _ \ 
 *	| |__| | |_| | | | | (_| |  __/ (_) | | | \__ \ | (_) | |   | |__| | (_) | (_) | (_) | | | | | |
 *	|_____/ \__,_|_| |_|\__, |\___|\___/|_| |_|___/  \___/|_|   |_____/ \___/ \___/ \___/|_| |_| |_|
 *                      __/  |                                                                      
 * 	       	           |____/                                                                       
 */

// import libraries necessary to handle exceptions when playing the game and connect to client through common socket.
import java.io.IOException;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

/**
 * This class is responsible for the server side of the communication.
 * The server stores the map that the players move around on.
 * Clients communicate with an instance of DungeonServer
 * The server stores the map and socket to create a common connection with the client.
 * A list of ClientConnections is compiled when a new client joins, starting a new thread per client.
 * 
 * @implements IServerFunctionality
 * @author 18181
 */
public class DungeonServer implements IServerFunctionality {
	
	GameEngineGUI gui; // hold an instance of the game engine GUI
	char[][] wholeMap;
	
	// define common port number, in keeping with the client side
	public static int PORT_NUMBER;
	
	// diagnostic variable to check connection still active
    protected static Boolean isListening = true;
    
    // dynamic list of clients connected to the server
    protected ArrayList<PlayerConnection> clientList = new ArrayList<PlayerConnection>();
    
    // game data from a birds eye view - player positions and the map they are playing on
    public int[][] playerPositions;
    protected Map map;
    
    // socket connections to establish communication with each new client
    protected Socket sock;
    private ServerSocket ssock;
    
	/**
	 * Constructor stores a record of the socket connection through which the server can listen to clients
	 * The map is set up only when when the server starts.
	 * An array to keep track of player positions on the map is created here.
	 * @param chosenMap 
	 * 
	 * @param sock 
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public DungeonServer(final int portNumber, String chosenMap) throws UnknownHostException, IOException {
		ssock = new ServerSocket(portNumber); // new server sockets to which clients connect
		setMap(chosenMap);
		playerPositions = new int[map.getMapHeight()][map.getMapWidth()];
		wholeMap = new char[map.getMapHeight()][map.getMapWidth()];
		
		// Schedule a job for the event-dispatching thread
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						// build the game engine GUI
						gui = new GameEngineGUI(portNumber, map.getMapWidth(), map.getMapHeight());
						gui.buildGUI();
						showMap(); // display the map on the GUI as well as its map name
						gui.changeText("Map name: " + map.getMapName());
					}
				});
				runGame();
	}
	
	/**
	 * Start listening on port
	 * Whilst game is active, keep listening out for new client requests.
	 * Accept connection once client makes contact.
	 * Once that happens, create a new thread which creates a new instance of PlayerConnection on its own thread
	 * i.e. allow multiple connections, each one with its own thread
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void runGame() throws UnknownHostException, IOException	{
		// whilst the server is listening on the port, accept new connections
		while (isListening) {
			sock = ssock.accept();
			// add the new connection to an arrayList to monitor clients
			try {
				clientList.add(new PlayerConnection(sock, this));
			} catch (Exception e) {
				gui.changeText("Too many clients are already connected. Client " + (clientList.size()+1) + " has been disconnected");
			}
		}
	}
	
	/**
	 * Accessor method returns the map char array.
	 * @return
	 */
	protected Map getMap(){
		return map;
	}

	/**
	 * Mutator method sets the map by loading the example map.
	 */
	public void setMap(String chosenMap) {
		map = new Map(new File("maps", chosenMap + ".txt"));
	}
	
	/**
	 * Prints whole view of map with players.
	 * @return
	 */
	public String showMap()	{
		String output = "";
		
		for (int i=0;i<map.getMap().length;i++){
			for (int j=0;j<map.getMap()[0].length;j++){
				if(playerPositions[i][j]==1)	{
					output += 'P';
					wholeMap[i][j] = 'P';
				}
				else	{
					output += map.getMap()[i][j];
					wholeMap[i][j] = map.getMap()[i][j];
				}
				
			}
			output += System.lineSeparator();
		}
		
		gui.printMap(wholeMap, map.getMapHeight(), map.getMapWidth());
		return output;
	}
	
	/**
	 * Method returns a diagnostic true/fale value for if a position on the map is taken by a player.
	 * No location in the dungeon can be occupied by two actors (i.e. human players or bots) simultaneously
	 * @param y
	 * @param x
	 * @return
	 */
	public boolean isTileTaken(int y, int x)	{
		
		if(playerPositions[y][x] == 1){ // if taken, return true
			return true;
		}
		else if(playerPositions[y][x] == 0){ // if free , return false
			return false;
		}
		return false;
		
	}
	
	/**
	 * Method updates the player positions 2D array when a new player is added to add them to the map.
	 * @param newY
	 * @param newX
	 */
	public void updatePlayerPositions(int newY, int newX) { // this version is to be called with
		for(int i=0; i<map.getMapHeight(); i++)	{
			for (int j=0; j<map.getMapWidth(); j++)	{
				if(i==newY && j==newX) {
					playerPositions[i][j]=1;
				}
			}
		}
	}
	
	/**
	 * Method updates the player positions 2D array to replace a player's old and new position when they move
	 * @param oldY
	 * @param oldX
	 * @param newY
	 * @param newX
	 */
	public void updatePlayerPositions(int oldY, int oldX, int newY, int newX) {
		for(int i=0; i<map.getMapHeight(); i++)	{
			for (int j=0; j<map.getMapWidth(); j++)	{
				if(i==oldY && j==oldX) {
					playerPositions[i][j]=0;
				}
			}
		}
		
		for(int i=0; i<map.getMapHeight(); i++)	{
			for (int j=0; j<map.getMapWidth(); j++)	{
				if(i==newY && j==newX) {
					playerPositions[i][j]=1;
				}
			}
		}
	}

	public void toggleIsListening() {
		if(isListening==true){
			isListening=false;
		}
		else if(isListening==false){
			isListening=true;
		}
	}
	
}
///**
// * INNER CLASS - PlayerConnection
// * This inner class represents a new client connection being made with the server.
// * When a new client connection is accepted, a new PlayerConnection object storing the client's attributes is added to a list.
// * This inner class runs as a thread, to listen for data sent from the client to be interpreted by the server.
// * 
// * @implements Runnable, IGameLogic
// * @author 18181
// */
//class PlayerConnection implements Runnable, IGameLogic {
//
//		// set out client attributes - a unique ID, position, gold collected
//		public int playerID;
//		public int counter;
//		public int[] playerPosition;
//		private int collectedGold;
//		BufferedReader dataFromClient;
//		PrintWriter dataToClient;
//		
//		// whether client connection active
//		private boolean active;
//		
//		// declare socket to which client listens and communicates with server
//		Socket listenAtSocket;
//		// new Thread variable to launch each player in a new thread.
//		private Thread connectionThread;
//		
//		/**
//		 * PlayerConnection constructor takes in the socket used to connect them
//		 * @param sock
//		 * @throws Exception 
//		 */
//		public PlayerConnection(Socket sock) throws Exception {
//			// set a unique client ID and initialise attributes
//			this.playerID = clientList.size()+1;
//			gui.changeText("Client " + playerID + " has connected.");
//			collectedGold = 0;
//			active = true;
//			// set a random initial position on the map
//			playerPosition = initiatePlayerPosition();
//			// if this isn't null, update the map accordingly and create a new thread to store this new PlayerConnection object
//			if(playerPosition!=null){
//				updatePlayerPositions(playerPosition[0], playerPosition[1]);
//				showMap();
//				listenAtSocket = sock;
//				
//				// Get input from client
//				dataFromClient = new BufferedReader(new InputStreamReader(listenAtSocket.getInputStream()));
//				// Send response to client
//				dataToClient = new PrintWriter(listenAtSocket.getOutputStream(), true);
//				
//				connectionThread = new Thread(this);
//				connectionThread.start(); // start new thread
//			}
//			else if(playerPosition==null){ // if position null, print error
//				try {
//					sock.close(); // close socket
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				
//				throw new Exception();
//			}
//			
//		}
//		
//		/**
//		 * Accessor method for player ID.
//		 * @return
//		 */
//		public int getPlayerID() {
//			return playerID;
//		}
//
//		/**
//		 * Mutator method for player ID.
//		 * @param playerID
//		 */
//		public void setPlayerID(int playerID) {
//			this.playerID = playerID;
//		}
//
//		/**
//		 * Accessor method for player position.
//		 * @return
//		 */
//		public int[] getPlayerPosition() {
//			return playerPosition;
//		}
//
//		/**
//		 * Mutator method for player position
//		 * @param playerPosition
//		 */
//		public void setPlayerPosition(int[] playerPosition) {
//			this.playerPosition = playerPosition;
//		}
//		
//		/**
//		 * Finds a random position for the player in the map.
//		 * @return Return null; if no position is found or a position vector [y,x]
//		 */
//		public synchronized int[] initiatePlayerPosition() {
//			int[] pos = new int[2]; // create position array for individual player
//			Random rand = new Random();
//			
//			// attempt random position on map
//			pos[0]=rand.nextInt(map.getMapHeight());
//			pos[1]=rand.nextInt(map.getMapWidth());
//			
//			int counter = 1;
//			// check position isn't a wall tile or taken by another player
//			while(map.lookAtTile(pos[0], pos[1]) == '#' || isTileTaken(pos[0], pos[1])) {
//				pos[0]=(int) ( counter * Math.sin(counter));
//				pos[1]= (int) ( counter * Math.cos(counter));
//				counter++;
//				
//				// if number of tries to assign position > maximum, send a null position
//				if(counter > map.getMapHeight() * map.getMapWidth()){
//					pos = null;
//					break;
//				}
//			}
//			
//			return pos;
//		}
//
//		/**
//		 * Thread run method which continuously listens for input from the user.
//		 */
//		@Override
//		public void run() {
//			try {
//				String input = "";
//				
//				// whilst there is an active connection and input from the user isn't null
//				while((input = dataFromClient.readLine())!=null)
//					/**
//					 * Whilst game still active, print game response to screen.
//					 */
//					
//						if(gameRunning())	{
//							// send game response back
//							if(gui.isListeningSelected()){
//									dataToClient.println(parseCommand(input)); // interpret input and print response back to client
//							}
//							else {
//								dataToClient.println("Server is currently not listening to client requests."); 
//							}
//							
//							if(checkWin()){
//								dataToClient.println("You have won the game!"); 
//								System.exit(0); // if game won, close the connection
//							}
//				}
//				
//				parseCommand("QUIT");
//			}
//			// handle exception
//			catch (IOException e) {
//				gui.changeText("Client " + getPlayerID() + " has disconnected."); // print message saying client has disconnected
//				quitGame();
//			}
//		}
//		
//		/**
//		 * Parsing and Evaluating the User Input.
//		 * @param readUserInput input the user generates
//		 * @return answer of GameLogic
//		 * @throws IOException 
//		 */
//		protected String parseCommand(String readUserInput) throws IOException {
//			String [] command = readUserInput.trim().split(" "); // split the user input and read the first word
//			String answer = "FAIL";
//			
//			// deal with each command accordingly.
//			// if command isn't listed, default response is "FAIL", printed back to the client
//			// this shows an unsuccessful command or invalid input
//			switch (command[0].toUpperCase())	{
//				case "HELLO":
//					answer = hello();
//					break;
//				case "MOVE":
//					if (command.length == 2) {
//						answer = move(command[1].charAt(0));
//					}
//					else {
//						answer = "FAIL";
//					}
//				break;
//				case "PICKUP":
//					answer = pickup();
//					break;
//				case "MAP":
//					answer = showMap();
//					break;
//				case "WIN":
//					answer = "Win: " + Integer.toString(map.getWin());
//					break;
//				case "ID":
//					answer = Integer.toString(getPlayerID());
//					break;
//				case "LOOK":
//					answer = look();
//					break;
//				case "QUIT":
//					quitGame();
//					break;
//					
//				default:
//					answer = "FAIL";
//				}
//			
//			return answer;
//		}
//
//		/**
//		 * Prints how much gold is still required to win!
//		 */
//		public String hello() {
//			return "GOLD: " + (map.getWin() - collectedGold);
//		}
//		
////		public native int hello(int winTotal, int collectedGold);
////		static {
////				System.loadLibrary("hello");
////		}
//
//		/**
//		 * By proving a character direction from the set of {N,S,E,W} the gamelogic 
//		 * checks if this location can be visited by the player. 
//		 * If it is true, the player is moved to the new location.
//		 * @return If the move was executed Success is returned. If the move could not execute Fail is returned.
//		 */
//		public synchronized String move(char direction) {
//			int oldY = playerPosition[0];
//			int oldX = playerPosition[1]; // store old X and Y position
//			
//			// alter the position to see what new position would be after the move takes place
//			int[] newPosition = getPlayerPosition().clone();
//			
//			switch (direction){
//				case 'N':
//					newPosition[0] -=1;
//					break;
//				case 'E':
//					newPosition[1] +=1;
//					break;
//				case 'S':
//					newPosition[0] +=1;
//					break;
//				case 'W':
//					newPosition[1] -=1;
//					break;
//				default:
//					return "FAIL";
//			}
//			
//			// if tile isn't a wall and isn't taken by another player, set new position to current position (move to new position)
//			if(map.lookAtTile(newPosition[0], newPosition[1]) != '#' && !isTileTaken(newPosition[0], newPosition[1])){
//				int newY = newPosition[0];
//				int newX = newPosition[1];
//				
//				setPlayerPosition(newPosition); // set new position and update array according to this
//				updatePlayerPositions(oldY, oldX, newY, newX);
//				
//				// check if this move satisfies winning criteria
//				if (checkWin())	{
//					dataToClient.println("You have won the game!"); 
//					quitGame();
//					for(int i=0; i< clientList.size(); i++){
//						try {
//							clientList.get(i).listenAtSocket.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//					isListening = false;
//				}
//				
//				showMap();
//				
//				try {
//					dataToClient.println(parseCommand("LOOK"));
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				
//				// if move successful, print to terminal
//				return "SUCCESS";
//			} else {
//				return "FAIL"; // otherwise, print error about fail
//			}
//		}
//		
//		/**
//		 * Method looks to see if tile is a gold tile.
//		 * If so, increment gold count for player.
//		 * 		Replace tile with an empty tile.
//		 * 		Print success message.
//		 * Else, print fail message.
//		 */
//		public String pickup() {
//
//			if (map.lookAtTile(playerPosition[0], playerPosition[1]) == 'G') {
//				collectedGold++;
//				map.replaceTile(playerPosition[0], playerPosition[1], '.');
//				try {
//					dataToClient.println(parseCommand("HELLO"));
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				return "SUCCESS, GOLD COINS: " + collectedGold;
//			}
//
//			return "FAIL" + "\n" + "There is nothing to pick up...";
//		}
//
//		/**
//		 * The method shows the dungeon around the player location.
//		 */
//		public String look() {
//			String output = "";
//			int playerXpos = clientList.get(playerID-1).playerPosition[1];
//			int playerYpos = clientList.get(playerID-1).playerPosition[0];
//			
//			char [][] lookReply = map.lookWindow(playerPositions, playerYpos, playerXpos, 5);
//			lookReply[2][2] = 'P';
//			
//			for (int i=0;i<lookReply.length;i++){
//				for (int j=0;j<lookReply[0].length;j++){
//					output += lookReply[j][i];
//				}
//				output += System.lineSeparator();
//			}
//			return output;
//		}
//		
//		/**
//		 * checks if the player collected all GOLD and is on the exit tile
//		 * @return True if all conditions are met, false otherwise
//		 */
//		protected boolean checkWin() {
//			if (collectedGold >= map.getWin() && 
//					map.lookAtTile(playerPosition[0], playerPosition[1]) == 'E') {
//				dataToClient.println("Congratulations!!! \n You have escaped the Dungeon of Dooom!!!!!! \n"
//						+ "Thank you for playing!");
//				return true;
//			}
//			return false;
//		}
//
//		/**
//		 * Quits the game when called
//		 */
//		public void quitGame() {
//			active = false;
//			playerPositions[playerPosition[0]][playerPosition[1]]=0;
//			showMap();
//			try {
//				sock.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		/**
//		 * Checks if the game is active.
//		 */
//		public boolean gameRunning(){
//			return active;
//		}
//		
//	}