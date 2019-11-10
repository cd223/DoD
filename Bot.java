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

// import libraries to handle exceptions and generate random numbers.
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * The Bot class acts as as a replacement for PlayGame - it represents a CPU-controlled player.
 * The game loop moves the Bot randomly around the map.
 * The Bot automatically picks up any gold that it lands on.
 * The Bot itself acts as a client, and is treated as so by the server.
 * 
 * @author 18181
 * @extends DungeonClient
 */
public class Bot extends DungeonClient {
	
	// declare object to generate random numbers
	private Random random;
	
	// declare position variables for target and current position
	int targetIPos;
	int targetJPos;
	int botIPos = 2;
	int botJPos = 2;
	
	// offset variables to calculate distance between target and current tile
	int iOffset = 0;
	int jOffset = 0;
	
	// diagnostics to check if the gold or exit is in view
	boolean goldInView = false;
	boolean exitInView = false;
	
	// calculate distance to target using heuristics
	double distance;
	
	ArrayList<String> pathList;
	
	// set the possible moves a Bot may complete according to the game protocol
	private static final String [] MOVEMENTS = {"MOVE N", "MOVE S", "MOVE W", "MOVE E"};
	String chosenCommand;
	
	/**
	 * Bot constructor inherits attributes from DungeonClient
	 * A new random object is created such that the choice of MOVE directions can be decided upon.
	 * @param playerType, portNumber, ipAddress
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public Bot(int portNumber, String ipAddress, int playerType) throws UnknownHostException, IOException{
		super(portNumber, ipAddress, playerType);
		random = new Random();
		pathList = new ArrayList<String>();
		chosenCommand = MOVEMENTS[random.nextInt(MOVEMENTS.length)]; // pick a command for the bot to follow at random
		update();
	}
	
	/**
	 * Method checks if a connection is still open between client and server.
	 */
	public boolean isThereConnection()	{
		return socket.isConnected(); // if the common socket is closed, the connection must be lost
	}
	
	/**
	 * Whilst a game is still active, this method prints the response to a bot's move to the terminal.
	 * This acts as the main game loop.
	 * The bot moves at random around the map.
	 * Any gold is picked up automatically.
	 * @throws IOException 
	 */
	public void update() {
		
		try{
			while (isThereConnection()){ // whilst a connection is active between client and server, read the user input
				String userCommand = "";
					
					try {
						TimeUnit.MILLISECONDS.sleep(500 + random.nextInt(1500)); // delay the bot's actions so that it moves at a reasonable pace (every 1 second)
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
//					determinePath(); // calculate path to target
					
					if(gui.textArea.getText().contains("Socket closed")){
					}
					else if (gui.textArea.getText().contains("Game won!")){
					}
					else if(socket.isClosed()){
					}
					else{
						userCommand =  MOVEMENTS[random.nextInt(MOVEMENTS.length)]; // pick a command for the bot to follow at random
						responseFailed = false;
						gui.changeText("\n" + userCommand + " and PICKUP");
						sendToServer(userCommand); // send that command to the server to deal with
						
						// aim to pickup after each move so that any gold is automatically picked up
						userCommand = "PICKUP";
						sendToServer(userCommand);
					}	
			}
			if(!isThereConnection()){ // if the connection is lost, close the socket
				socket.close();
			}
		}
		catch(IOException IOE) {
		}
	}
	
	/**
	 * Determines whether a new direction should be selected.
	 * Ensures the bot doesn't move entirely randomly.
	 * @return
	 */
	public String chooseDirection(){
		String reply = "";
		
		// if list of moves to make is empty, make new move
		if(pathList == null || pathList.isEmpty()){
			if(responseFailed){
				reply = MOVEMENTS[random.nextInt(MOVEMENTS.length)]; // pick a command for the bot to follow at random
			}
		} // otherwise, choose move based on list of stored moves
		else if(!pathList.isEmpty()){
			reply = pathList.remove(pathList.size()-1);
		}
		
		return reply;
	}
	
	/**
	 * A method which listens to see if the lookWindow ArrayList is occupied.
	 * Decisions about which direction to turn are made here.
	 */
	public void determinePath(){
		locateTarget(); // looks for gold or exit based on if there is still gold to pick up.
		
		if(exitInView || goldInView){
			LinkedList<Node> determinedPath;
			Node startNode = new Node();
			Node goalNode = new Node();
				
			// determine start and end positions (nodes)
			startNode.nodePosition[0] = botIPos;
			startNode.nodePosition[1] = botJPos;
			goalNode.nodePosition[0] = targetIPos;
			goalNode.nodePosition[1] = targetJPos;
			
			// create a path between the two points
			determinedPath = search(startNode, goalNode);
			
			// add commands to pathList
			iOffset = determinedPath.get(0).nodePosition[0] - botIPos;
	    	jOffset = determinedPath.get(0).nodePosition[1] - botJPos;
			
	    	while(iOffset!=0){
		    	if(iOffset>0){
		    		pathList.add("MOVE S");
		    		iOffset--;
		    	}
		    	else if(iOffset<0){
		    		pathList.add("MOVE N");
		    		iOffset++;
		    	}
	    	}
	    	
	    	while(jOffset!=0){
		    	if(jOffset>0){
		    		pathList.add("MOVE E");
		    		jOffset--;
		    	}
		    	else if(jOffset<0){
		    		pathList.add("MOVE W");
		    		jOffset++;
		    	}
	    	}
			
	    	// determine movements to end node
		      for(int i = 0; i < determinedPath.size()-1; i++){
		    	Node currentNode = determinedPath.get(i);
		    	Node nextNode = determinedPath.get(i+1);
		        
		    	iOffset = nextNode.nodePosition[0] - currentNode.nodePosition[0];
		    	jOffset = nextNode.nodePosition[1] - currentNode.nodePosition[1];
		    	
		    	while(iOffset!=0){
			    	if(iOffset>0){
			    		pathList.add("MOVE S");
			    		iOffset--;
			    	}
			    	else if(iOffset<0){
			    		pathList.add("MOVE N");
			    		iOffset++;
			    	}
		    	}
		    	
		    	while(jOffset!=0){
			    	if(jOffset>0){
			    		pathList.add("MOVE E");
			    		jOffset--;
			    	}
			    	else if(jOffset<0){
			    		pathList.add("MOVE W");
			    		jOffset++;
			    	}
		    	}
		    	
		      }
		}
		
		exitInView = false;
		goldInView = false;
		lookWindow.clear();
	}
	
	/**
	 * path from the start node to the goal node as a linked-list
	 * pathParent node is the node's parent in the path list.
	 * Once the goal is found, the path can be found by traversing up the path list from the goal node to the start node
	 * assumes the start node has no parent in the path
	 * @param node
	 * @return
	 * @reference http://www.peachpit.com/articles/article.aspx?p=101142
	 */
	protected LinkedList<Node> constructPath(Node node) {
	  LinkedList<Node> path = new LinkedList<Node>();
	  while (node.pathParent != null) {
	    path.addFirst(node);
	    node = node.pathParent;
	  }
	  return path;
	}
	
	/**
	   * function returns a list of nodes that represent the path, not including start node. 
	   * If path can't be found, it returns null.
	   * keep track of all nodes that have been visited by putting them in a "closed" list
	   * if node already in "closed" list, ignore it. 
	   * keep track of all nodes we want to visit in "open" list.
	   * "open" list FIFO sorting list from smallest number of edges from the start goal to the largest
	   * @param startNode
	   * @param goalNode
	   * @return
	   * @reference http://www.peachpit.com/articles/article.aspx?p=101142
	   */
	  public LinkedList<Node> search(Node startNode, Node goalNode) {
		  // list of visited nodes
		  LinkedList<Node> closedList = new LinkedList<Node>();
		  
		  // list of nodes to visit (sorted)
		  LinkedList<Node> openList = new LinkedList<Node>();
		  openList.add(startNode);
		  startNode.pathParent = null;
		  
		  while (!openList.isEmpty()) {
		    Node node = (Node)openList.removeFirst();
		    
		    if (node == goalNode) {
		      // path found!
		      return constructPath(goalNode);
		    }
		    else {
		      closedList.add(node);
		      
		      // add the neighbours of the current node
		      addNeighbours(node);
		      
		      // add neighbours to the open list
		      
		      for(int i=0; i< node.neighbours.size(); i++){
		    	  Node neighborNode = node.neighbours.get(i);
			        if (!closedList.contains(neighborNode) && !openList.contains(neighborNode)) {
			          neighborNode.pathParent = node;
			          openList.add(neighborNode);
			        }
		      }
		    }
		  }
		  
		  // no path found
		  return null;
		}
	
	  
	  public void locateTarget(){
			 if(!lookWindow.isEmpty()){ 
					if(goldToPickUp) {
						for(int i=0; i<lookWindow.size(); i++)	{
							for(int j=0; j<lookWindow.get(i).length; j++) {
								if(lookWindow.get(i)[j] == 'G'){
									targetIPos = i;
									targetJPos = j;
									goldInView = true;
									break;
								}
							}
						
							if(goldInView){
								break;
							}
						}
					}
					else if(!goldToPickUp) {
						for(int i=0; i<lookWindow.size(); i++)	{
							for(int j=0; j<lookWindow.get(i).length; j++) {
								if(lookWindow.get(i)[j] == 'E'){
									targetIPos = i;
									targetJPos = j;
									exitInView = true;
									break;
								}
							}
						
							if(exitInView){
								break;
							}
						}
					}
				}
		 }
	  
	  /**
	   * populate a list of neighbours for a given node.
	   * @param currentNode
	   */
	  public void addNeighbours(Node currentNode){
		
		  int nodeIPos = currentNode.nodePosition[0];
		  int nodeJPos = currentNode.nodePosition[1];
			 
			  if(nodeIPos+1 < 5){
				 if(!(lookWindow.get(nodeIPos+1)[nodeJPos] == 'X') && !(lookWindow.get(nodeIPos+1)[nodeJPos] == '#')){
					 Node down = new Node();
					 down.nodePosition[0] = nodeIPos+1;
					 down.nodePosition[1] = nodeJPos;
					 currentNode.neighbours.add(down);
				 }
			  }
			  
			  if(nodeJPos+1 < 5){
					 if(!(lookWindow.get(nodeIPos)[nodeJPos+1] == 'X') && !(lookWindow.get(nodeIPos)[nodeJPos+1] == '#')){
						 Node right = new Node();
						 right.nodePosition[0] = nodeIPos;
						 right.nodePosition[1] = nodeJPos+1;
						 currentNode.neighbours.add(right);
					 }
			  }
			  
			  if(nodeIPos-1 > -1){
					 if(!(lookWindow.get(nodeIPos-1)[nodeJPos] == 'X') && !(lookWindow.get(nodeIPos-1)[nodeJPos] == '#')){
						 Node up = new Node();
						 up.nodePosition[0] = nodeIPos-1;
						 up.nodePosition[1] = nodeJPos;
						 currentNode.neighbours.add(up);
					 }
			  }
				  
			  if(nodeJPos-1 > -1){
					 if(!(lookWindow.get(nodeIPos)[nodeJPos-1] == 'X') && !(lookWindow.get(nodeIPos)[nodeJPos-1] == '#')){
						 Node left = new Node();
						 left.nodePosition[0] = nodeIPos;
						 left.nodePosition[1] = nodeJPos-1;
						 currentNode.neighbours.add(left);
					 }
			  }
	  	}
	
	 /**
	  * A class which stores the position and neighbours of a given node.
	  * The parent is the node which should precede this current node in the path.
	  * @author Chris
	  * @reference http://www.peachpit.com/articles/article.aspx?p=101142
	  */
	class Node	{
		//a basic node, which has a list of all its neighbours
		ArrayList<Node> neighbours;
		int[] nodePosition;
		Node pathParent; //used for searching only
		
		public Node(){
			neighbours = new ArrayList<Node>();
			nodePosition = new int[2];
			pathParent = null;
		}
	}
	
}