import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
/**
 * This inner class represents a new client connection being made with the server.
 * When a new client connection is accepted, a new PlayerConnection object storing the client's attributes is added to a list.
 * This inner class runs as a thread, to listen for data sent from the client to be interpreted by the server.
 * 
 * @implements Runnable, IGameLogic
 * @author 18181
 */
public class PlayerConnection implements Runnable, IGameLogic {

		// set out client attributes - a unique ID, position, gold collected
		public int playerID;
		DungeonServer server;
		public int counter;
		public int[] playerPosition;
		private int collectedGold;
		BufferedReader dataFromClient;
		PrintWriter dataToClient;
		
		// whether client connection active
		private boolean active;
		
		// declare socket to which client listens and communicates with server
		private Socket listenAtSocket;
		// new Thread variable to launch each player in a new thread.
		private Thread connectionThread;
		
		/**
		 * PlayerConnection constructor takes in the socket used to connect them
		 * @param sock
		 * @param dungeonServer2 
		 * @throws Exception 
		 */
		public PlayerConnection(Socket sock, DungeonServer dungeonServer) throws Exception {
			this.server = dungeonServer;
			// set a unique client ID and initialise attributes
			this.playerID = server.clientList.size()+1;
			server.gui.changeText("Client " + playerID + " has connected.");
			collectedGold = 0;
			active = true;
			// set a random initial position on the map
			playerPosition = initiatePlayerPosition();
			// if this isn't null, update the map accordingly and create a new thread to store this new PlayerConnection object
			if(playerPosition!=null){
				server.updatePlayerPositions(playerPosition[0], playerPosition[1]);
				server.showMap();
				listenAtSocket = sock;
				
				// Get input from client
				dataFromClient = new BufferedReader(new InputStreamReader(listenAtSocket.getInputStream()));
				// Send response to client
				dataToClient = new PrintWriter(listenAtSocket.getOutputStream(), true);
				
				connectionThread = new Thread(this);
				connectionThread.start(); // start new thread
			}
			else if(playerPosition==null){ // if position null, print error
				try {
					sock.close(); // close socket
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				throw new Exception();
			}
		}

		/**
		 * Accessor method for player ID.
		 * @return
		 */
		public int getPlayerID() {
			return playerID;
		}

		/**
		 * Mutator method for player ID.
		 * @param playerID
		 */
		public void setPlayerID(int playerID) {
			this.playerID = playerID;
		}

		/**
		 * Accessor method for player position.
		 * @return
		 */
		public int[] getPlayerPosition() {
			return playerPosition;
		}

		/**
		 * Mutator method for player position
		 * @param playerPosition
		 */
		public void setPlayerPosition(int[] playerPosition) {
			this.playerPosition = playerPosition;
		}
		
		/**
		 * Finds a random position for the player in the map.
		 * @return Return null; if no position is found or a position vector [y,x]
		 */
		public synchronized int[] initiatePlayerPosition() {
			int[] pos = new int[2]; // create position array for individual player
			Random rand = new Random();
			
			// attempt random position on map
			pos[0]=rand.nextInt(server.map.getMapHeight());
			pos[1]=rand.nextInt(server.map.getMapWidth());
			
			int counter = 1;
			// check position isn't a wall tile or taken by another player
			while(server.map.lookAtTile(pos[0], pos[1]) == '#' || server.isTileTaken(pos[0], pos[1])) {
				pos[0]=(int) ( counter * Math.sin(counter));
				pos[1]= (int) ( counter * Math.cos(counter));
				counter++;
				
				// if number of tries to assign position > maximum, send a null position
				if(counter > server.map.getMapHeight() * server.map.getMapWidth()){
					pos = null;
					break;
				}
			}
			
			return pos;
		}

		/**
		 * Thread run method which continuously listens for input from the user.
		 */
		@Override
		public void run() {
			try {
				String input = "";
				
				// whilst there is an active connection and input from the user isn't null
				while((input = dataFromClient.readLine())!=null)
					/**
					 * Whilst game still active, print game response to screen.
					 */
					
						if(gameRunning())	{
							// send game response back
							if(server.gui.isListeningSelected()){
									dataToClient.println(parseCommand(input)); // interpret input and print response back to client
							}
							else {
								dataToClient.println("Server is currently not listening to client requests."); 
							}
							
							if(checkWin()){
								dataToClient.println("You have won the game!"); 
								System.exit(0); // if game won, close the connection
							}
				}
				
				parseCommand("QUIT");
			}
			// handle exception
			catch (IOException e) {
				server.gui.changeText("Client " + getPlayerID() + " has disconnected."); // print message saying client has disconnected
				quitGame();
			}
		}
		
		/**
		 * Parsing and Evaluating the User Input.
		 * @param readUserInput input the user generates
		 * @return answer of GameLogic
		 * @throws IOException 
		 */
		protected String parseCommand(String readUserInput) throws IOException {
			String [] command = readUserInput.trim().split(" "); // split the user input and read the first word
			String answer = "FAIL";
			
			// deal with each command accordingly.
			// if command isn't listed, default response is "FAIL", printed back to the client
			// this shows an unsuccessful command or invalid input
			switch (command[0].toUpperCase())	{
				case "HELLO":
					answer = hello();
					break;
				case "MOVE":
					if (command.length == 2) {
						answer = move(command[1].charAt(0));
					}
					else {
						answer = "FAIL";
					}
				break;
				case "PICKUP":
					answer = pickup();
					break;
				case "MAP":
					answer = server.showMap();
					break;
				case "WIN":
					answer = "Win: " + Integer.toString(server.map.getWin());
					break;
				case "ID":
					answer = Integer.toString(getPlayerID());
					break;
				case "LOOK":
					answer = look();
					break;
				case "QUIT":
					quitGame();
					break;
					
				default:
					answer = "FAIL";
				}
			
			return answer;
		}

		/**
		 * Prints how much gold is still required to win!
		 */
		public String hello() {
			return "GOLD: " + (server.map.getWin() - collectedGold);
		}

		/**
		 * By proving a character direction from the set of {N,S,E,W} the gamelogic 
		 * checks if this location can be visited by the player. 
		 * If it is true, the player is moved to the new location.
		 * @return If the move was executed Success is returned. If the move could not execute Fail is returned.
		 */
		public synchronized String move(char direction) {
			int oldY = playerPosition[0];
			int oldX = playerPosition[1]; // store old X and Y position
			
			// alter the position to see what new position would be after the move takes place
			int[] newPosition = getPlayerPosition().clone();
			
			switch (direction){
				case 'N':
					newPosition[0] -=1;
					break;
				case 'E':
					newPosition[1] +=1;
					break;
				case 'S':
					newPosition[0] +=1;
					break;
				case 'W':
					newPosition[1] -=1;
					break;
				default:
					return "FAIL";
			}
			
			// if tile isn't a wall and isn't taken by another player, set new position to current position (move to new position)
			if(server.map.lookAtTile(newPosition[0], newPosition[1]) != '#' && !server.isTileTaken(newPosition[0], newPosition[1])){
				int newY = newPosition[0];
				int newX = newPosition[1];
				
				setPlayerPosition(newPosition); // set new position and update array according to this
				server.updatePlayerPositions(oldY, oldX, newY, newX);
				
				// check if this move satisfies winning criteria
				if (checkWin())	{
					dataToClient.println("You have won the game!"); 
					quitGame();
					for(int i=0; i< server.clientList.size(); i++){
						try {
							server.clientList.get(i).listenAtSocket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					server.toggleIsListening();
				}
				
				server.showMap();
				
				try {
					dataToClient.println(parseCommand("LOOK"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// if move successful, print to terminal
				return "SUCCESS";
			} else {
				return "FAIL"; // otherwise, print error about fail
			}
		}
		
		/**
		 * Method looks to see if tile is a gold tile.
		 * If so, increment gold count for player.
		 * 		Replace tile with an empty tile.
		 * 		Print success message.
		 * Else, print fail message.
		 */
		public String pickup() {

			if (server.map.lookAtTile(playerPosition[0], playerPosition[1]) == 'G') {
				collectedGold++;
				server.map.replaceTile(playerPosition[0], playerPosition[1], '.');
				try {
					dataToClient.println(parseCommand("HELLO"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return "SUCCESS, GOLD COINS: " + collectedGold;
			}

			return "FAIL" + "\n" + "There is nothing to pick up...";
		}

		/**
		 * The method shows the dungeon around the player location.
		 */
		public String look() {
			String output = "";
			int playerXpos = server.clientList.get(playerID-1).playerPosition[1];
			int playerYpos = server.clientList.get(playerID-1).playerPosition[0];
			
			char [][] lookReply = server.map.lookWindow(server.playerPositions, playerYpos, playerXpos, 5);
			lookReply[2][2] = 'P';
			
			for (int i=0;i<lookReply.length;i++){
				for (int j=0;j<lookReply[0].length;j++){
					output += lookReply[j][i];
				}
				output += System.lineSeparator();
			}
			return output;
		}
		
		/**
		 * checks if the player collected all GOLD and is on the exit tile
		 * @return True if all conditions are met, false otherwise
		 */
		protected boolean checkWin() {
			if (collectedGold >= server.map.getWin() && 
					server.map.lookAtTile(playerPosition[0], playerPosition[1]) == 'E') {
				dataToClient.println("Congratulations!!! \n You have escaped the Dungeon of Dooom!!!!!! \n"
						+ "Thank you for playing!");
				return true;
			}
			return false;
		}

		/**
		 * Quits the game when called
		 */
		public void quitGame() {
			active = false;
			server.playerPositions[playerPosition[0]][playerPosition[1]]=0;
			server.showMap();
			try {
				server.sock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Checks if the game is active.
		 */
		public boolean gameRunning(){
			return active;
		}
		
	}