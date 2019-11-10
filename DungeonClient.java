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

// import libraries necessary to establish a connection from client to server.
// import libraries necessary to handle exceptions during these processes.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

/**
 * The class encapsulates the functions expected of the client.
 * A port number and socket are defined.
 * A thread is used to continually listen and print feedback received from the server.
 * Implements runnable to achieve this threading.
 * 
 * @author 18181
 * @implements Runnable
 */
public class DungeonClient implements Runnable {

	PlayerGUI gui; // hold an instance of the player GUI
	
	// Open connection to server listening to specified port on localhost
	protected static int PORT_NUMBER;
	protected static String IP_ADDRESS;
	Socket socket;
	
	// Store server response as a string
	protected boolean goldToPickUp;
	protected ArrayList<char[]> lookWindow;
	
	// declare input and output streams to connect to the server as well as to read user input 
	BufferedReader serverOutput;
	PrintWriter sendToServer;
	protected boolean connectionActive;
	protected boolean responseFailed;
		
	/**
	 * Constructor connects Scanner and PrintWriter to socket connection
	 * Starts a new thread to continually read server feedback on a loop.
	 * Prints server feedback when it is not null.
	 * @param playerType 
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public DungeonClient(final int portNumber, final String ipAddress, final int playerType) throws UnknownHostException, IOException{
		// create new socket under fixed port number
		socket = new Socket(ipAddress, portNumber);
		connectionActive = true;
		
		// Open BufferedReader to manage response from server
		serverOutput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		// Open PrintWriter to send response to server
		sendToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		
		lookWindow = new ArrayList<char[]>();
		
		// Schedule a job for the event-dispatching thread
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						// build a new player GUI
						gui = new PlayerGUI(sendToServer, ipAddress, portNumber, playerType);
						gui.buildGUI();
						// send look and hello to the server so the player view can be constructed
						sendToServer("LOOK");
						sendToServer("HELLO");
					}
				});
		
				
		new Thread(this).start(); // start listening on server feedback in background
		goldToPickUp = true;
	}
	
	/**
	 * Sends user input to the server ready to be parsed as a command and evaluated.
	 * @param userInput
	 */
	public void sendToServer(String userInput){;
		// guard for if the user is sending null input to the server
		if(userInput!=null){
			// if the user is trying to quit, exit the program
			if(userInput.toUpperCase().equals("QUIT")){
				System.exit(0);
			}
			// otherwise, send the command to the server.
			else	{ 
				sendToServer.println(userInput);
			}
		}
	}
	
	/**
	 * A thread which listens on a loop to the server, printing any feedback to commands to the terminal.
	 */
	@Override
	public void run()	{
		
		String serverResponse;
		
		try {
			while((serverResponse = serverOutput.readLine()) != null) { // whilst there is feedback
				
				// check if the previous response failed. If so, set the boolean to true
				if(serverResponse.equals("FAIL")){
					responseFailed = true;
				}
				
				// check if the player has won the game. If so, run the method pushing this info to the GUI
				if(serverResponse.equals("You have won the game!")){
					gui.gameWon();
					connectionActive = false;
				}
				
				// if the response is a LookReply, capture the information in an arraylist ready for processing
				if(serverResponse.length() == 5) {
					char[] row = new char[5];
					
					for(int i=0; i<5; i++){
						row[i] = serverResponse.charAt(i);
					}
					
					lookWindow.add(row);
					
					// if the lookwindow arrayList is 'full' (stores a complete 5x5 view), send it to the GUI
					if(lookWindow.size()==5){
						gui.printLookWindow(lookWindow);
						lookWindow.clear();
					}
					
				}
				
				// otherwise if the response is to a hello command, update the remaining gold on the GUI
				else if(serverResponse.contains(" ")){
					if(serverResponse.split(" ")[0].equals("GOLD:") && Integer.parseInt(serverResponse.split(" ")[1])>0){
						goldToPickUp = true;
						gui.updateGoldRemaining(Integer.parseInt(serverResponse.split(" ")[1]));
					}
					else if(serverResponse.split(" ")[0].equals("GOLD:") && Integer.parseInt(serverResponse.split(" ")[1])==0){
						goldToPickUp = false;
						gui.updateGoldRemaining(Integer.parseInt(serverResponse.split(" ")[1]));
					}
					else if(serverResponse.split(" ")[0].equals("Win:") && Integer.parseInt(serverResponse.split(" ")[1])>0){
						gui.updateMaxGold(Integer.parseInt(serverResponse.split(" ")[1]));
					}
				}
				// otherwise, print what the server response is to the GUI
				else{
					gui.changeText(serverResponse); // print the feedback to the client
				}
				
			}
		} catch (IOException e) {
			gui.changeText("Socket closed.");
			
			try {
				socket.close(); // if the output from the server ever results in an exception, close the connection
				gui.disableButtons();
			} catch (IOException e1) {
				gui.changeText("Socket closure unsuccessful."); // if this is unsuccessful, print an error
				
			}
		}
	}
}