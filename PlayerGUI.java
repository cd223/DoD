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

// import libraries necessary to build and show the GUI.
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class PlayerGUI {

	// declare components as public so that they can be updated from any method
	JFrame window;
	JTextArea textArea;
	JPanel middlePanel;
	JLabel goldRemaining;
	JProgressBar progress;
	
	// declare variables to set grid constraints
	GridBagConstraints gbc;
	
	// declare a variable to store the maximum number of gold collected before a win
	int maxGold;
	
	// declare buttons to move the player around in the dungeon and pick up gold
	JButton moveNorth;
	JButton moveSouth;
	JButton moveEast;
	JButton moveWest;
	JButton pickup;
	JButton getID;
	
	// declare an array to store the lookWindow grid
	char[][] lookWindowArray;
	
	// declare variables to store data passed to the GUI to help build the map and display server information
	PrintWriter sendToServer;
	int PORT_NUMBER;
	int playerType;
	String IPAddress;

	/**
	 * Constructor method for the player GUI sets local variables to corresponding parameters.
	 * @param sendToServer
	 * @param ipAddress
	 * @param portNumber
	 * @param playerType
	 */
	public PlayerGUI(PrintWriter sendToServer, String ipAddress, int portNumber, int playerType) {
		this.sendToServer = sendToServer;
		this.IPAddress = ipAddress;
		this.PORT_NUMBER = portNumber;
		this.playerType = playerType; // 0 human, 1 bot
	}

	/**
	 * This method will update the text pane on screen to append the piece of text passed to it.
	 * @param text
	 */
	public void changeText(String text){
		if(!text.isEmpty()){
			textArea.setText(textArea.getText() + "\n" + text);
		}
	}
	
	/**
	 * This method changes a string into a multiline string inserting line breaks
	 * @param original
	 * @return
	 */
	public String convertToMultiline(String original)
	{
	    return "<html>" + original.replaceAll("\n", "<br>");
	}
	
	/**
	 * This method sends to the server any piece of text sent to it.
	 * @param text
	 */
	public void sendToServer(String text) {
		sendToServer.println(text);
	}
	
	/**
	 * This method builds the actual player GUI. It declares and places all components on a common JFrame.
	 */
	public void buildGUI(){
		// create window frame with default title, its behaviour on close and dimension
		window = new JFrame();
		
		// display the title as either bot or human mode depending on which type of player is playing
		if(playerType==0){
			window.setTitle("Dungeon of Dooom - Player Mode");
		}
		else if(playerType ==1){
			window.setTitle("Dungeon of Dooom - Bot Mode");
		}
		
		window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		window.setLocationByPlatform(true);
		window.setAutoRequestFocus(false);
		window.setAlwaysOnTop(false);
		window.setSize(new Dimension(1200, 700));
		
		// declare panels to place in the window frame
		middlePanel = new JPanel();
		JPanel topPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		
		// set the colours of the panels
		middlePanel.setBackground(Color.darkGray);
		topPanel.setBackground(Color.BLACK);
		leftPanel.setBackground(Color.darkGray);
		rightPanel.setBackground(Color.BLACK);
		
		// set the sizes of the panels
		middlePanel.setPreferredSize(new Dimension(500, 500));
		topPanel.setPreferredSize(new Dimension(600, 150));
		leftPanel.setPreferredSize(new Dimension(600, 500));
		rightPanel.setPreferredSize(new Dimension(200, 500));
		
		// place panels in the frame
		window.getContentPane().add(middlePanel, BorderLayout.CENTER);
		window.getContentPane().add(topPanel, BorderLayout.PAGE_START);
		window.getContentPane().add(leftPanel, BorderLayout.LINE_START);
		window.getContentPane().add(rightPanel, BorderLayout.LINE_END);
			
		// change top panel layout to FlowLayout with left alignment
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
			topPanel.setLayout(flowLayout);
		middlePanel.setLayout(new GridBagLayout());
		
		if(playerType == 0){ // if a human is playing
			// create labels for button titles
			JLabel movementsTitle = new JLabel("MOVEMENTS");
				movementsTitle.setForeground(Color.WHITE);
				movementsTitle.setFont(new Font("Lucida Console", Font.BOLD, 16));
			JLabel commandsTitle = new JLabel("COMMANDS");
				commandsTitle.setForeground(Color.WHITE);
				commandsTitle.setFont(new Font("Lucida Console", Font.BOLD, 16));
			
			// add buttons to control movements
			moveNorth = new JButton("NORTH");
			moveSouth = new JButton("SOUTH");
			moveWest = new JButton("WEST");
			moveEast = new JButton("EAST");
			pickup = new JButton("PICKUP");
			getID = new JButton("ID");
			
			// set button colours and icons
						moveNorth.setBackground(new Color(0,79,198));
							moveNorth.setForeground(Color.WHITE);
							moveNorth.setFocusPainted(false);
							moveNorth.setFont(new Font("Lucida Console", Font.BOLD, 16));
								ImageIcon oldMoveN = new ImageIcon("img/moveN.png");
								Image img = oldMoveN.getImage() ;  
								Image newimg = img.getScaledInstance(20, 30,  java.awt.Image.SCALE_SMOOTH ) ;  
								ImageIcon moveN = new ImageIcon( newimg );
							moveNorth.setIcon(moveN);
							
						moveSouth.setBackground(new Color(0,79,198));
							moveSouth.setForeground(Color.WHITE);
							moveSouth.setFocusPainted(false);
							moveSouth.setFont(new Font("Lucida Console", Font.BOLD, 16));
								ImageIcon oldMoveS = new ImageIcon("img/moveS.png");
								Image img2 = oldMoveS.getImage() ;  
								Image newimg2 = img2.getScaledInstance(20, 30,  java.awt.Image.SCALE_SMOOTH ) ;  
								ImageIcon moveS = new ImageIcon(newimg2);
							moveSouth.setIcon(moveS);
							
						moveWest.setBackground(new Color(0,79,198));
							moveWest.setForeground(Color.WHITE);
							moveWest.setFocusPainted(false);
							moveWest.setFont(new Font("Lucida Console", Font.BOLD, 16));
								ImageIcon oldMoveW = new ImageIcon("img/moveW.png");
								Image img3 = oldMoveW.getImage() ;  
								Image newimg3 = img3.getScaledInstance(30, 20,  java.awt.Image.SCALE_SMOOTH ) ;  
								ImageIcon moveW = new ImageIcon(newimg3);
							moveWest.setIcon(moveW);
							
						moveEast.setBackground(new Color(0,79,198));
							moveEast.setForeground(Color.WHITE);
							moveEast.setFocusPainted(false);
							moveEast.setFont(new Font("Lucida Console", Font.BOLD, 16));
								ImageIcon oldMoveE = new ImageIcon("img/moveE.png");
								Image img4 = oldMoveE.getImage() ;  
								Image newimg4 = img4.getScaledInstance(30, 20,  java.awt.Image.SCALE_SMOOTH ) ;  
								ImageIcon moveE = new ImageIcon(newimg4);
							moveEast.setIcon(moveE);
							
						pickup.setBackground(new Color(0,175,140));
							pickup.setForeground(Color.BLACK);
							pickup.setFocusPainted(false);
							pickup.setPreferredSize(new Dimension(150,25));
							pickup.setFont(new Font("Lucida Console", Font.BOLD, 16));
							pickup.setIcon(new ImageIcon("img/pickup.png"));
								ImageIcon oldPickup = new ImageIcon("img/pickup.png");
								Image img5 = oldPickup.getImage() ;  
								Image newimg5 = img5.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH ) ;  
								ImageIcon pickupIcon = new ImageIcon(newimg5);
							pickup.setIcon(pickupIcon);
							
						getID.setBackground(new Color(0,175,140));
							getID.setForeground(Color.BLACK);
							getID.setFocusPainted(false);
							getID.setPreferredSize(new Dimension(150,25));
							getID.setFont(new Font("Lucida Console", Font.BOLD, 16));
							ImageIcon oldID = new ImageIcon("img/ID.png");
							Image img6 = oldID.getImage() ;  
							Image newimg6 = img6.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH ) ;  
							ImageIcon IDIcon = new ImageIcon(newimg6);
						getID.setIcon(IDIcon);
							
						ImageIcon oldLook = new ImageIcon("img/look.png");
						Image img7 = oldLook.getImage() ;  
						Image newimg7 = img7.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH ) ;  
						JLabel lookIcon = new JLabel(new ImageIcon(newimg7));
						middlePanel.add(lookIcon);
						
							// add title and buttons to right hand pane
							rightPanel.add(movementsTitle);
							rightPanel.add(moveNorth);
							rightPanel.add(moveSouth);
							rightPanel.add(moveWest);
							rightPanel.add(moveEast);
							rightPanel.add(commandsTitle);
							rightPanel.add(pickup);
							rightPanel.add(getID);
							
							// add action listener to move north button to send the "MOVE N" command on click to the server
							moveNorth.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									// if the socket is closed or the game has been won, tell the user to click X
									if(textArea.getText().contains("Socket closed.")){
										changeText("\n" + "Socket is no longer accepting input. Press X at the top right corner to close.");
									}
									else if(textArea.getText().contains("Game won!")){
										changeText("\n" + "The game has been won. Press X or Quit to close.");
									}
									else {
										changeText("\n" + "MOVE N");
										sendToServer("MOVE N");
									}
								}
							});
							
							// add action listener to move south button to send the "MOVE S" command on click to the server
							moveSouth.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									// if the socket is closed or the game has been won, tell the user to click X
									if(textArea.getText().contains("Socket closed.")){
										changeText("\n" + "Socket is no longer accepting input. Press X at the top right corner to close.");
									}
									else if(textArea.getText().contains("You have won the game!")){
										changeText("\n" + "The game has been won. Press X or Quit to close.");
									}
									else {
										changeText("\n" + "MOVE S");
										sendToServer("MOVE S");
									}
								}
							});
							
							// add action listener to move west button to send the "MOVE W" command on click to the server
							moveWest.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									// if the socket is closed or the game has been won, tell the user to click X
									if(textArea.getText().contains("Socket closed.")){
										changeText("\n" + "Socket is no longer accepting input. Press X at the top right corner to close.");
									}
									else if(textArea.getText().contains("You have won the game!")){
										changeText("\n" + "The game has been won. Press X or Quit to close.");
									}
									else {
										changeText("\n" + "MOVE W");
										sendToServer("MOVE W");
									}
								}
							});
							
							// add action listener to move east button to send the "MOVE E" command on click to the server
							moveEast.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									// if the socket is closed or the game has been won, tell the user to click X
									if(textArea.getText().contains("Socket closed.")){
										changeText("\n" + "Socket is no longer accepting input. Press X at the top right corner to close.");
									}
									else if(textArea.getText().contains("You have won the game!")){
										changeText("\n" + "The game has been won. Press X or Quit to close.");
									}
									else {
										changeText("\n" + "MOVE E");
										sendToServer("MOVE E");
									}
								}
							});
							
							// add action listener to pickup button to send the "PICKUP" command on click to the server
							pickup.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									// if the socket is closed or the game has been won, tell the user to click X
									if(textArea.getText().contains("Socket closed.")){
										changeText("\n" + "Socket is no longer accepting input. Press X at the top right corner to close.");
									}
									else if(textArea.getText().contains("You have won the game!")){
										changeText("\n" + "The game has been won. Press X or Quit to close.");
									}
									else {
										changeText("\n" + "PICKUP");
										sendToServer("PICKUP");
									}
								}
							});
							
							// add action listener to get ID button to send the "ID" command on click to the server
							getID.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									// if the socket is closed or the game has been won, tell the user to click X
									if(textArea.getText().contains("Socket closed.")){
										changeText("\n" + "Socket is no longer accepting input. Press X at the top right corner to close.");
									}
									else if(textArea.getText().contains("You have won the game!")){
										changeText("\n" + "The game has been won. Press X or Quit to close.");
									}
									else {
										changeText("\n" + "ID");
										sendToServer("ID");
									}
								}
							});
		}
		
		// declare and initialise a button to quit the game
		JButton quit = new JButton("QUIT");
		
			quit.setBackground(new Color(198,0,66));
				quit.setForeground(Color.WHITE);
				quit.setFocusPainted(false);
				quit.setPreferredSize(new Dimension(150,25));
				quit.setFont(new Font("Lucida Console", Font.BOLD, 16));
					ImageIcon oldQuit = new ImageIcon("img/quit.png");
					Image img8 = oldQuit.getImage() ;  
					Image newimg8 = img8.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH ) ;  
					ImageIcon quitIcon = new ImageIcon(newimg8);
				quit.setIcon(quitIcon);
			
			rightPanel.add(quit);
		
		// add a label to monitor the count for the gold remaining before a player can exit the dungeon
		goldRemaining = new JLabel();
			goldRemaining.setBackground(new Color(25,0,255));
			goldRemaining.setForeground(new Color(255,222,0));
			goldRemaining.setFont(new Font("Lucida Console", Font.BOLD, 16));
		rightPanel.add(goldRemaining);
			
		// add a progress bar to accompany the gold remaining count - monitors player progress through the game
		progress = new JProgressBar();
			progress.setMinimum(0);
			progress.setValue(0);
			sendToServer("WIN");
			progress.setName("Progress");
			progress.setForeground(new Color(255,222,0));
			progress.setBackground(new Color(25,0,255));
			progress.setStringPainted(true);
		
		rightPanel.add(progress);
		
		// create text area and ensure it is not editable
		textArea = new JTextArea(15, 30);
		// print that the server is being connected to
		textArea.setText("Connecting to server...");
		// report the IP address and port number of the server the client has connected to
		textArea.setText(textArea.getText() + "\n" + "Connected to " + IPAddress + " on port " + PORT_NUMBER);
		textArea.setFont(new Font("Lucida Console", Font.BOLD, 16));
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(new Color(0,255,9));
		// add the text pane to an autoscrolling scroll area.
		JScrollPane scrollArea = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollArea.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());				
			}
		});
		leftPanel.add(scrollArea);
		
		// if the player is a human, add instructions for how to control the player to a label 
			JLabel instructions = new JLabel();
			StringBuilder sb = new StringBuilder();
			if(playerType ==0){
				sb.append("\nMOVE:");
				sb.append("\nMove one square (N, E, S, W).");
				sb.append("\n");
				sb.append("\nPICKUP:");
				sb.append("\nPick up item in current location.");
				sb.append("\n");
				sb.append("\nID:");
				sb.append("\nPrint your player ID.");
				sb.append("\n");
				sb.append("\nQUIT:");
				sb.append("\nClose the window and exit the game.");
			}
			// if the player is a bot, add a notice explaining the movement of the bot
			else if(playerType ==1){
				sb.append("\nBOT MODE:");
				sb.append("\nThis is a game in bot mode.");
				sb.append("\n");
				sb.append("\nAll moves and commands are made automatically.");
				sb.append("\n");
				sb.append("\nThe progress of the bot through the game can be seen in this window.");
			}
			instructions.setText(convertToMultiline(sb.toString()));
				instructions.setBackground(Color.BLACK);
				instructions.setForeground(Color.WHITE);
				instructions.setFont(new Font("Lucida Console", Font.BOLD, 16));
				instructions.setPreferredSize(new Dimension(400,200));
			leftPanel.add(instructions);
		
			ImageIcon oldLogo = new ImageIcon("img/logo.jpg");
			Image img1 = oldLogo.getImage() ;  
			Image newimg1 = img1.getScaledInstance(750, 150,  java.awt.Image.SCALE_SMOOTH ) ;  
			ImageIcon logo = new ImageIcon( newimg1 );
				JLabel logoLabel = new JLabel(logo);
				topPanel.add(logoLabel);
		
		// add action listener to quit button to send the quit command to the server
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textArea.getText().contains("Socket closed.")){
					changeText("\n" + "Socket is no longer accepting input. Press X at the top right corner to close.");
				}
				else if(textArea.getText().contains("You have won the game!")){
					changeText("\n" + "The game has been won. Press X or Quit to close.");
				}
				else {
					changeText("\n" + "QUIT");
					sendToServer("QUIT");
					window.dispose();
				}
			}
		});
		
		window.pack();
		window.setVisible(true);
		
		// every 0.5 seconds, update the player window to reflect changes going on around the player
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
				  sendToServer("LOOK");
				  sendToServer("HELLO");
			  }
		}, 500, 500);
	}

	/**
	 * This method repaints the middle panel by updating the look window when a new movement is made by the player.
	 * It receives the look window array list with players overlaid as a parameter.
	 * This method traverses the array list, populates an array with the data then 
	 * traverses this to choose the appropriate icon for each tile type.
	 * @param lookWindow
	 */
	public void printLookWindow(ArrayList<char[]> lookWindow) {
		// remove existing components from the panel and set new constraints
		middlePanel.removeAll();
		JLabel temp;
		gbc = new GridBagConstraints();
		lookWindowArray = new char[5][5];
		
		// traverse through the arrayList and populate a 2D array with its data
		for(int x=0; x<5; x++){
			for(int y=0; y<5; y++){
				lookWindowArray[x][y] = lookWindow.get(x)[y];
			}
		}
		
		// traverse through the 2D array
		for (int i=0;i<5;i++){
			for (int j=0;j<5;j++){
				
				// set the grid i,j co-ordinate that the new component will be added to
				gbc.gridx = j;
				gbc.gridy = i;
				
				// if tile is a wall tile, add that icon to the appropriate square on the grid
				if(lookWindowArray[i][j]=='#')	{
					ImageIcon wallTileIcon = new ImageIcon( "img/lwWallTile.png" );
					temp = new JLabel(wallTileIcon);
					middlePanel.add(temp, gbc);
				}
				// if tile is an empty tile, add that icon to the appropriate square on the grid
				else if(lookWindowArray[i][j]=='.'){
					ImageIcon emptyTileIcon = new ImageIcon( "img/lwEmptyTile.png" );
					temp = new JLabel(emptyTileIcon);
					middlePanel.add(temp, gbc);
				}
				// if tile is a player tile, add that icon to the appropriate square on the grid
				else if(lookWindowArray[i][j]=='P'){
					// If it is the current player, add that icon
					if(i == 2 && j == 2){
						if(playerType == 0){ // if human, add the human icon
							ImageIcon playerTileIcon = new ImageIcon( "img/lwPlayerTile.png" );
							temp = new JLabel(playerTileIcon);
							middlePanel.add(temp, gbc);
						}
						else if(playerType == 1){ // if bot, add the bot icon
							ImageIcon botTileIcon = new ImageIcon( "img/lwBotTile.png" );
							temp = new JLabel(botTileIcon);
							middlePanel.add(temp, gbc);
						}
					}
					// otherwise add the 'other player' icon
					else {
						ImageIcon otherPlayerTileIcon = new ImageIcon( "img/lwOtherPlayerTile.png" );
						temp = new JLabel(otherPlayerTileIcon);
						middlePanel.add(temp, gbc);
					}
				}
				// if tile is a gold tile, add that icon to the appropriate square on the grid
				else if(lookWindowArray[i][j]=='G'){
					ImageIcon goldTileIcon = new ImageIcon( "img/lwGoldTile.png" );
					temp = new JLabel(goldTileIcon);
					middlePanel.add(temp, gbc);
				}
				// if tile is an exit tile, add that icon to the appropriate square on the grid
				else if(lookWindowArray[i][j]=='E'){
					ImageIcon exitTileIcon = new ImageIcon( "img/lwExitTile.png" );
					temp = new JLabel(exitTileIcon);
					middlePanel.add(temp, gbc);
				}
				// if tile is an X tile, add that icon to the appropriate square on the grid
				else if(lookWindowArray[i][j]=='X'){
					ImageIcon xTileIcon = new ImageIcon( "img/lwXTile.png" );
					temp = new JLabel(xTileIcon);
					middlePanel.add(temp, gbc);
				}
			}
		}
		
		window.setVisible(true);
		window.repaint();
		window.setAlwaysOnTop(false);
	}

	/**
	 * This method updates the label containing the amount of gold required to win/
	 * @param goldToGo
	 */
	public void updateGoldRemaining(int goldToGo) {
		// update the amount to be the number of pieces of gold passed into the method
		goldRemaining.setText("Gold to win: " + goldToGo);
		progress.setValue(maxGold-goldToGo); // set new "gold to win" remaining tally
	}
	
	/**
	 * This method sets the maximum number of pieces of gold required to win the game and escape
	 * @param maximum
	 */
	public void updateMaxGold(int maximum) {
		maxGold = maximum;
		progress.setMaximum(maximum);
	}

	/**
	 * This method prints the end of game congratulations message.
	 * It also disables buttons to ensure the socket has closed and guards against errors
	 */
	public void gameWon() {
		// print win message to the text area
		changeText("Game won!");
		
		// remove all labels from the middle panel
		middlePanel.removeAll();
		JLabel temp = new JLabel();
		
		// traverse through the current look window array
		for(int i=0; i<5;i++){
			for(int j=0; j<5; j++){
				
				// // set the grid i,j co-ordinate that the new component will be added to
				gbc.gridx = j;
				gbc.gridy = i;
				
				// if the tile is in the 3rd row down, add the corresponding win message tile
				if(i==2 && j==0){
					ImageIcon escapeIcon = new ImageIcon( "img/escaped1.png" );
					temp = new JLabel(escapeIcon);
					middlePanel.add(temp, gbc);
				}
				else if(i==2 && j==1){
					ImageIcon escapeIcon = new ImageIcon( "img/escaped2.png" );
					temp = new JLabel(escapeIcon);
					middlePanel.add(temp, gbc);
				}
				else if(i==2 && j==2){
					ImageIcon escapeIcon = new ImageIcon( "img/escaped3.png" );
					temp = new JLabel(escapeIcon);
					middlePanel.add(temp, gbc);		
				}
				else if(i==2 && j==3){
					ImageIcon escapeIcon = new ImageIcon( "img/escaped4.png" );
					temp = new JLabel(escapeIcon);
					middlePanel.add(temp, gbc);
				}
				else if(i==2 && j==4){
					ImageIcon escapeIcon = new ImageIcon( "img/escaped5.png" );
					temp = new JLabel(escapeIcon);
					middlePanel.add(temp, gbc);
				}
				// otherwise, if the if tile is a wall tile, add that icon to the appropriate square on the grid
				else if(lookWindowArray[i][j]=='#')	{
					ImageIcon wallTileIcon = new ImageIcon( "img/lwWallTile.png" );
					temp = new JLabel(wallTileIcon);
					middlePanel.add(temp, gbc);
				}
				// otherwise, if the if tile is an empty tile, add that icon to the appropriate square on the grid
				else if(lookWindowArray[i][j]=='.'){
					ImageIcon emptyTileIcon = new ImageIcon( "img/lwEmptyTile.png" );
					temp = new JLabel(emptyTileIcon);
					middlePanel.add(temp, gbc);
				}
				// otherwise, if the if tile is a player tile, add that icon to the appropriate square on the grid
				else if(lookWindowArray[i][j]=='P'){
					if(i == 2 && j == 2){ // if the current player
						if(playerType == 0){ // if human, add human icon
							ImageIcon playerTileIcon = new ImageIcon( "img/lwPlayerTile.png" );
							temp = new JLabel(playerTileIcon);
							middlePanel.add(temp, gbc);
						}
						else if(playerType == 1){ // if bot, add bot icon
							ImageIcon botTileIcon = new ImageIcon( "img/lwBotTile.png" );
							temp = new JLabel(botTileIcon);
							middlePanel.add(temp, gbc);
						}
					} // otherwise, if the if tile is another player tile, add that icon to the appropriate square on the grid
					else {
						ImageIcon otherPlayerTileIcon = new ImageIcon( "img/lwOtherPlayerTile.png" );
						temp = new JLabel(otherPlayerTileIcon);
						middlePanel.add(temp, gbc);
					}
				}
				// otherwise, if the if tile is a gold tile, add that icon to the appropriate square on the grid
				else if(lookWindowArray[i][j]=='G'){
					ImageIcon goldTileIcon = new ImageIcon( "img/lwGoldTile.png" );
					temp = new JLabel(goldTileIcon);
					middlePanel.add(temp, gbc);
				}
				// otherwise, if the if tile is a gold tile, add that icon to the appropriate square on the grid
				else if(lookWindowArray[i][j]=='E'){
					ImageIcon exitTileIcon = new ImageIcon( "img/lwExitTile.png" );
					temp = new JLabel(exitTileIcon);
					middlePanel.add(temp, gbc);
				}
				// otherwise, if the if tile is an X tile, add that icon to the appropriate square on the grid
				else if(lookWindowArray[i][j]=='X'){
					ImageIcon xTileIcon = new ImageIcon( "img/lwXTile.png" );
					temp = new JLabel(xTileIcon);
					middlePanel.add(temp, gbc);
				}
			}
		}
		
		if(playerType == 0){ // if human player
		disableButtons(); // disable the buttons on the window
		}
		window.setVisible(true);
		window.repaint();
		window.setAlwaysOnTop(false);
		
	}
	
	/**
	 * This method disables all the control buttons on the window so that the user cannot click them
	 * once a game has been won.
	 */
	public void disableButtons(){
		moveNorth.setEnabled(false);
		moveSouth.setEnabled(false);
		moveWest.setEnabled(false);
		moveEast.setEnabled(false);
		pickup.setEnabled(false);
		getID.setEnabled(false);
	}
	
}