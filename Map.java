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

// import libraries to handle exceptions reading from the map file and structures for storing map data 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reads a sample ASCII-art map text file and can load any map written in the same format.
 * Stores the map information in a 2D array (char[][]).
 * This class encapsulates all map characteristics.
 * It reads, loads and prints the map.
 * It also extracts the map name and the number of gold pieces required to win a game.
 * LookAtTile returns the type of tile the tile is, and ReplaceTile replaces it with another.
 * The LookWindow method returns the view a given player has around it.
 * 
 * @author 18181
 */
public class Map {
	
	// store the compiled map in 2D char array
	private char[][] map;
	
	// store name of map and the total number of gold pieces on the map.
	private String mapName;
	private int totalGoldOnMap;
	
	/**
	 * Constructor without parameters sets the map object to null
	 * Name of map is set to null.
	 * Default gold on map = -1.
	 */
	public Map(){
		map = null;
		mapName = "";
		totalGoldOnMap = -1;
	}
	
	/**
	 * Constructor with parameter resets map object by calling first constructor (without parameters).
	 * Reads new map file in.
	 * Calls method to set the map up - read it and load it into the game.
	 * @param mapFile
	 */
	public Map(File mapFile){
		this();
		readMap(mapFile);
	}
	
	/**
	 * Reads a map from a given file with the format: name <mapName>, win <totalGold>.
	 * Interprets the map text tile line by line and attempts to load it.
	 * 
	 * @param mapFile - A File pointed to a correctly formatted map file
	 */
	public void readMap(File mapFile) {
		// a buffered reader for the map data to be read from a text file
		BufferedReader reader = null;
		
		try { // try the reading map text file by connecting the BufferedReader to the file.
			reader = new BufferedReader(new FileReader(mapFile));
		} catch (FileNotFoundException e) {
			try { // if file not found, try loading the example map
				reader = new BufferedReader(new FileReader(new File("maps","example_map.txt")));
			} catch (FileNotFoundException e1) { // if this is still unsuccessful, print error message
				System.err.println("no valid map name given and default file example_map.txt not found");
				System.exit(-1); // quit program
			}
		}
		
		try { // try to load the map data read from the file
			map = loadMap(reader);
		} catch (IOException e){ // if unsuccessful, print an error
			System.err.println("map file invalid or wrongly formatted");
			System.exit(-1); // exit program
		} finally {
			try {
				reader.close(); // finally try to close the reader to prevent resource leaks
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Load a map from file using BufferedReader
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public char[][] loadMap(BufferedReader reader) throws IOException {
		// initialise error diagnostic to false
		boolean error = false;
		
		// set new 1D char array ArrayList and map width to -1
		ArrayList<char[]> tempMap = new ArrayList<char[]>();
		int width = -1;
		
		// read input to see if file starts with "name"
		String in = reader.readLine();
		if (in.startsWith("name")){
			error = setName(in); // set name and return success diagnostic
		}
		
		// read next input to see if file specifies "win"
		in = reader.readLine();
		if (in.startsWith("win")){
			error = setWin(in); // set win criteria and return success diagnostic
		}
		
		// read input from file, calculate width from length of line
		in = reader.readLine();
		if (in.charAt(0) == '#' && in.length() > 1)
			width = in.trim().length();
		
		// whilst there is still text to read and errors haven't occurred
		while (in != null && !error)
		{
			// create new 1D char array of length equal to the line
			char[] row = new char[in.length()];
			
			// return error if line length doesn't match width
			if  (in.length() != width)
				error = true;
			
			// sparse each tile on map to new element in 1D array
			for (int i = 0; i < in.length(); i++)
			{
				row[i] = in.charAt(i);
			}

			// add to element of temporary map in ArrayList
			tempMap.add(row);

			// read next line for loop to repeat with
			in = reader.readLine();
		}
		
		// handle error
		if (error) {
			setName("");
			setWin("");
			return null;
		}
		
		// create 2D array to store map.
		char[][] map = new char[tempMap.size()][width];
		
		// populate 2D array with each element (array) in ArrayList
		for (int i=0;i<tempMap.size();i++){
			map[i] = tempMap.get(i);
		}
		return map;
	}
	
	/**
	 * Set the number of gold coins required to win the game.
	 * @param in
	 * @return
	 */
	private boolean setWin(String in) {
		// if line doesn't start with win criteria, return error diagnostic as true
		if (!in.startsWith("win ")){
			return true;
		}
			
		int win = 0; // initialise the number of coins required to 0.
		
		try { // try to extract win total from partitioned string
			win = Integer.parseInt(in.split(" ")[1].trim()); 
		} catch (NumberFormatException n){ // if this is not successful, print an error
			System.err.println("the map does not contain a valid win criteria!");
		}
		
		// if the number required is negative, return an error
		if (win < 0) {
			return true;
		}
		
		// otherwise, set win total to appropriate variable
		this.totalGoldOnMap = win;
		
		return false;
	}

	/**
	 * Set the name of the map based on data stored within the file.
	 * @param in
	 * @return
	 */
	private boolean setName(String in) {
		// if name string doesn't start with "name " and is <4 characters, return an error
		if (!in.startsWith("name ") && in.length() < 4)
			return true;
		
		// extract the actual name from string
		String name = in.substring(4).trim();
		
		// if the name length is 0 or below then return an error
		if (name.length() < 1) {
			return true;
		}
		
		// otherwise, set the map name to the appropriate variable
		this.mapName = name;
		
		return false;
	}
	
	/**
	 * The method replaces a char at a given position of the map with a new char
	 * @param y the vertical position of the tile to replace
	 * @param x the horizontal position of the tile to replace
	 * @param tile the char character of the tile to replace
	 * @return The old character which was replaced will be returned.
	 */
	protected synchronized char replaceTile(int y, int x, char tile) {
		char output = map[y][x]; // store tile at desired location
		map[y][x] = tile; // set new tile to replaced tile
		return output;
	}
	
	/**
	 * Method prints the entire 2D map.
	 */
	protected void printMap(){
		for (int y = 0; y < getMapHeight(); y++) {
			for (int x = 0; x < getMapWidth(); x++) {
				System.out.print(map[y][x]);
			}
			System.out.println();
		}
	}
	
	/**
	 * The method returns the Tile at a given location. The tile is not removed.
	 * @param y the vertical position of the tile to replace
	 * @param x the horizontal position of the tile to replace
	 * @param tile the char character of the tile to replace
	 * @return The old character which was replaced will be returned.
	 */
	protected char lookAtTile(int y, int x) {
		if (y < 0 || x < 0 || y >= map.length || x >= map[0].length){
			return '#'; // look at case for wall tiles
		}
		
		char output = map[y][x]; // otherwise, return the element of the map being looked at
		
		return output;
	}
	
	/**
	 * This method is used to retrieve a map view around a certain location.
	 * The method should be used to get the look() around the player location.
	 * @param y Y coordinate of the location
	 * @param x X coordinate of the location
	 * @param radius The radius defining the area which will be returned. 
	 * Without the usage of a lamp the standard value is 5 units.
	 * @return
	 */
	protected char[][] lookWindow(int[][] playerPositions, int y, int x, int radius) {
		char[][] reply = new char[radius][radius]; // set array of the player's viewing scope
		
		for (int i = 0; i < radius; i++) {
			for (int j = 0; j < radius; j++) {
				int posX = x + j - radius/2;
				int posY = y + i - radius/2;
				if (posX >= 0 && posX < getMapWidth() && posY >= 0 && posY < getMapHeight()) // if 0 < x,y < width/height
					if(playerPositions[posY][posX]==1) // check if any other player exists in the view scope 
					{
						reply[j][i] = 'P'; // print a player symbol if one exists there
					}
					else {
						reply[j][i] = map[posY][posX]; // if a player does not reside in the window, print the map tile
					}
				else
					reply[j][i] = '#'; // otherwise print a wall tile
			}
		}
		
		// set edge tiles to an X
		reply[0][0] = 'X';
		reply[radius-1][0] = 'X';
		reply[0][radius-1] = 'X';
		reply[radius-1][radius-1] = 'X';
		
		return reply;
	}
	
	/**
	 * Accessor method returns the map array.
	 * @return
	 */
	public char[][] getMap()	{
		return map;
	}

	/**
	 * Accessor method returns the amount of gold on the map that is required to win.
	 * @return
	 */
	public int getWin() {
		return totalGoldOnMap;
	}

	/**
	 * Accessor method returns the map name.
	 * @return
	 */
	public String getMapName() {
		return mapName;
	}

	/**
	 * Accessor method returns the map width.
	 * @return
	 */
	protected int getMapWidth() {
		return map[0].length;
	}

	/**
	 * Accessor method returns the map height.
	 * @return
	 */
	protected int getMapHeight() {
		return map.length;
	}

}