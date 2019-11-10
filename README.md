         _____                                                  __   _____                              
	|  __ \                                                / _| |  __ \                             
	| |  | |_   _ _ __   __ _  ___  ___  _ __  ___    ___ | |_  | |  | | ___   ___   ___  _ __ ___  
	| |  | | | | | '_ \ / _` |/ _ \/ _ \| '_ \/ __|  / _ \|  _| | |  | |/ _ \ / _ \ / _ \| '_ ` _ \ 
	| |__| | |_| | | | | (_| |  __/ (_) | | | \__ \ | (_) | |   | |__| | (_) | (_) | (_) | | | | | |
	|_____/ \__,_|_| |_|\__, |\___|\___/|_| |_|___/  \___/|_|   |_____/ \___/ \___/ \___/|_| |_| |_|
		       	    __/  |                                                                      
			   |____/                                                                       

	The Dungeon of Dooom is played on a rectangular grid (the Dungeon) on which the player can move and pick up items. 
	The goal is to collect enough gold and make it to the exit.
	You start the game with no gold, and at a random location within the dungeon. 
	This position may contain gold (if you are lucky), may be an empty tile, or it may be an exit tile. 
	The objective of the game is to collect at least a certain amount of gold and then move onto an exit tile in the dungeon. 
	If you have enough gold and land on the exit, you automatically leave the dungeon and the game finishes.

					   
		IMPORTANT: Start the game by running the StartServer.java GUI BEFORE any other classes.
					Game runs best using the Eclipse IDE.
This starts up the GUI responsible for launching the server and loads the default map.
There is a field for the user to enter the port number they wish to launch the server with (a number from 1-65536).
Clicking Start Server will launch the server onto this chosen port number.


The map is stored on the Server and only there. 
The client doesn't have a copy of it.
The map is selected by the server and initialised upon the server starting.

Dungeon of Dooom works on a client/server model. The server stores the map.

Next, to connect the client, run StartClient.java GUI which allows a client to choose a port number and IP address of server
to connect to. The user then chooses to launch a human player or a bot into the server of that IP address on the specified port.

Both humans and bots act as types of client for human and CPU players.

All Game Logic and server-related code is stored server side, with an inner class
for eachnew client connection. This holds a reference to the client's socket, ID and position.

The bot AI uses a Breadth First algorithm to determine a path to its target (gold or an exit).

Both humans and bots may be run simultaneously and play on the same map.
Multiple players can play on the same map. These players can be of bot or human type.

The code allows you to play Dungeon of Dooom against your bot in the same dungeon.
It also supports friends and their bots joining you in the dungeon.

No location in the dungeon can be occupied by two actors (i.e. human players or bots)
simultaneously. This means if a player occupies a given location, no other player can successfully move to
that location on the map.

Bots not much smarter, faster & better than humans. They run on a random delay timer before making another move.
This makes them playable on a human time scale.

The bots and humans are avoided against walking through walls. 
The bot picks up any gold that it lands on automatically.

Typing MOVE, LOOK, QUIT, HELLO and PICKUP works as per the original protocol.

Typing ID or MAP produces the client's ID and the full map view of the current game with alll players positioned.