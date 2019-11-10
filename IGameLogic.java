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
 * Hides implementation detail of the methods required to play the game
 * Defines the methods, which correspond to commands in the game protocol
 * Implemented by the PlayerConnection inner class of the DungeonServer class.
 * 
 * @author 18181
 */
public interface IGameLogic {
		
	// set out methods to be implemented in all classes that implement IGameLogic
	// hide implementation detail of method signatures - to be implemented as concrete methods in PlayerConnection class
	public String hello();
	public String move(char direction);
	public String pickup();
	public String look();
	public boolean gameRunning();
	public void quitGame();
	
}
