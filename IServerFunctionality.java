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

/**
 * Hides implementation detail of the methods required to run the server.
 * Defines the methods, which correspond to server functions 
 * (as the server holds the map and information about player positions).
 * Implemented by the DungeonServer class.
 * 
 * @author 18181
 */
public interface IServerFunctionality {

	// methods to set the map up at the start of a game and to reposition a given player
	public void setMap(String chosenMap);
	public void updatePlayerPositions(int oldY, int oldX, int newY, int newX);
	
}
