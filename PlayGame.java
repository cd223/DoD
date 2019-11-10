/**

 *   _____                                                  __   _____                              
 *	|  __ \                                                / _| |  __ \                             
 *	| |  | |_   _ _ __   __ _  ___  ___  _ __  ___    ___ | |_  | |  | | ___   ___   ___  _ __ ___  
 *	| |  | | | | | '_ \ / _` |/ _ \/ _ \| '_ \/ __|  / _ \|  _| | |  | |/ _ \ / _ \ / _ \| '_ ` _ \ 
 *	| |__| | |_| | | | | (_| |  __/ (_) | | | \__ \ | (_) | |   | |__| | (_) | (_) | (_) | | | | | |
 *	|_____/ \__,_|_| |_|\__, |\___|\___/|_| |_|___/  \___/|_|   |_____/ \___/ \___/ \___/|_| |_| |_|
 *                      __/  |                                                                      
 * 	       	           |____/                                                                       
 * 
 *
 *  Dungeon of Dooom is played on a rectangular grid (the Dungeon) on which the
 *	player can move and pick up items. The goal is to collect enough gold and make it to
 *	the exit.
 *
 * @author 18181
 * @date 10/03/2016
 */

// import libraries to handle exceptions
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * The PlayGame class sets up a client connection to the server by calling its parent constructor.
 * A welcome message is displayed instructing the client how to play the game.
 * PlayGame extends DungeonClient, establishing a connection before the game starts.
 * 
 * @author 18181
 * @extends DungeonClient
 */
public class PlayGame extends DungeonClient {
	
	/**
	 * Constructor calls its super constructor to establish input/output streams 
	 * to write to/from a common socket with the server.
	 * @param playerType 
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public PlayGame(int portNumber, String ipAddress, int playerType) throws UnknownHostException, IOException {
		super(portNumber, ipAddress, playerType);
		update();
	}
	
	/**
	 * Method checks if a connection is still open between client and server.
	 */
	public boolean isThereConnection()	{
		return !socket.isClosed(); // if the common socket is closed, the connection must be lost
	}
	
	/**
	 * Whilst a game is still active, this method prints the response to a client's move to the terminal.
	 * This acts as the main game loop.
	 * @throws IOException 
	 */
	public void update() {
		
		try{
			while (isThereConnection()){ // whilst a connection is active between client and server, read the user input
			}
			if(!isThereConnection()){ // if the connection is lost, close the socket
				socket.close();
			}
		}
		catch(IOException IOE) {
		}
	}
}