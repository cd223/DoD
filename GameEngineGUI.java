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
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;

/**
 * This class is responsible for running the actual server and providing an interface for the game engine.
 * It displays a god's eye view of the map with players overlaid on it
 * It reports the IP for the server.
 * It allows the user to turn listening to clients on/off.
 * @author cjd47
 */
public class GameEngineGUI {
	
	// declare a radio button to turn on/off listening to clients
	JRadioButton listening;
	
	// declare the panel where the map will be stored as well as the text pane as public so that they can be updated from any method
	JPanel leftPanel;
	JTextArea textArea;
	JFrame window;
	
	// declare variables to store data passed to the GUI to help build the map and display server information
	int PORT_NUMBER;
	int mapWidth;
	int mapHeight;
	
	/**
	 * Constructor method for the game engine GUI sets local variables to corresponding parameters.
	 * @param portNumber
	 * @param mapWidth
	 * @param mapHeight
	 */
	public GameEngineGUI(int portNumber, int mapWidth, int mapHeight){
		this.PORT_NUMBER = portNumber;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
	}
	
	/**
	 * This method will update the text pane on screen to append the piece of text passed to it.
	 * @param text
	 */
	public void changeText(String text){
		textArea.setText(textArea.getText() + "\n" + text);
	}
	
	/**
	 * This method returns a boolean diagnostic to determine whether the radio button is selected.
	 * @return
	 */
	public boolean isListeningSelected(){
		return listening.isSelected();
	}
	
	/**
	 * This method builds the actual game engine GUI. It declares and places all components on a common JFrame.
	 */
	public void buildGUI(){
		// create window frame with default title, its behaviour on close and dimension
		window = new JFrame("Dungeon of Dooom - Game Engine GUI");
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setLocationByPlatform(true);
		window.setAutoRequestFocus(false);
		window.setAlwaysOnTop(false);
		window.setSize(new Dimension(1200, 700));
		
		// declare panels to place in the window frame
		JPanel middlePanel = new JPanel();
		JPanel topPanel = new JPanel();
		leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		
		// set the colours of the panels
		middlePanel.setBackground(Color.DARK_GRAY);
		topPanel.setBackground(Color.BLACK);
		leftPanel.setBackground(Color.DARK_GRAY);
		rightPanel.setBackground(Color.BLACK);
		
		// set the sizes of the panels
		middlePanel.setPreferredSize(new Dimension(650, 500));
		topPanel.setPreferredSize(new Dimension(700, 150));
		leftPanel.setPreferredSize(new Dimension(800, 200));
		rightPanel.setPreferredSize(new Dimension(200, 100));
		
		// place panels in the frame
		window.getContentPane().add(middlePanel, BorderLayout.CENTER);
		window.getContentPane().add(topPanel, BorderLayout.PAGE_START);
		window.getContentPane().add(leftPanel, BorderLayout.LINE_START);
		window.getContentPane().add(rightPanel, BorderLayout.LINE_END);
		
		// change top panel layout to FlowLayout with left alignment
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
			topPanel.setLayout(flowLayout);
		leftPanel.setLayout(new GridBagLayout());
		
		// create labels for button titles
		JLabel commandsTitle = new JLabel("COMMANDS");
			commandsTitle.setForeground(Color.WHITE);
			commandsTitle.setFont(new Font("Lucida Console", Font.BOLD, 16));
		
		// add buttons to control panel
		JButton shutDown = new JButton("SHUTDOWN");
			shutDown.setBackground(new Color(198,0,66));
				shutDown.setForeground(Color.WHITE);
				shutDown.setFont(new Font("Lucida Console", Font.BOLD, 16));
				shutDown.setFocusPainted(false);
				ImageIcon oldShutdown = new ImageIcon("img/shutdown.png");
				Image img = oldShutdown.getImage() ;  
				Image newimg = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH ) ;  
				ImageIcon shutdownIcon = new ImageIcon( newimg );
			shutDown.setIcon(shutdownIcon);
			
		// add title and buttons to right hand pane
			rightPanel.add(commandsTitle);
			rightPanel.add(shutDown);
		
		// create text area and ensure it is not editable
		textArea = new JTextArea(18, 25);
		//report the port number of the server
		textArea.setText(textArea.getText() + "Running server on port " + PORT_NUMBER);
		// report the IP address of the server
		try{
			InetAddress ip = InetAddress.getLocalHost();
			textArea.setText(textArea.getText() + "\n" + "Server IP address: " + ip);
		} catch(UnknownHostException uhe){
		}
		textArea.setFont(new Font("Lucida Console", Font.BOLD, 16));
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(new Color(0,255,9));
		
		// wrap the text area in a scrollarea which will autoscroll when new text is appended
		JScrollPane scrollArea = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollArea.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());				
			}
		});
		// add the scroll area to the middle panel
		middlePanel.add(scrollArea);
		
		// declare and set up a a radio button which will toggle the listening to clients
		listening = new JRadioButton("Listen to clients?");
			listening.setSelected(true);
			listening.setBackground(Color.BLACK);
			listening.setFont(new Font("Lucida Console", Font.BOLD, 16));
			listening.setForeground(new Color(0,255,9));
		middlePanel.add(listening);
		
		// add the game label to the top panel
		ImageIcon oldLogo = new ImageIcon("img/logo.jpg");
		Image img1 = oldLogo.getImage() ;  
		Image newimg1 = img1.getScaledInstance(750, 150,  java.awt.Image.SCALE_SMOOTH ) ;  
		ImageIcon logo = new ImageIcon( newimg1 );
			JLabel logoLabel = new JLabel(logo);
			topPanel.add(logoLabel);
		
		// add action listener to the shut down button
		shutDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// print message saying the server will close when the button is pressed
				changeText("\n" + "Closing server...");
				// close the server
				System.exit(0);
			}
		});
			
		window.pack();
		window.setVisible(true);
	}

	/**
	 * This method repaints the left panel by updating the map when a new movement is made by any player.
	 * It receives the whole map with players overlaid as a parameter.
	 * This method traverses the array, choosing the appropriate icon for each tile type.
	 * @param wholeMap
	 * @param height
	 * @param width
	 */
	public void printMap(char[][] wholeMap, int height, int width) {
		// remove existing components from the panel and set new constraints
		leftPanel.removeAll();
		JLabel temp;
		GridBagConstraints gbc = new GridBagConstraints();
		
		// traverse through the 2D array
		for (int i=0;i<height;i++){
			for (int j=0;j<width;j++){
				
				// set the grid i,j co-ordinate that the new component will be added to
				gbc.gridx = j;
				gbc.gridy = i;
				
				// if tile is a wall tile, add that icon to the appropriate square on the grid
				if(wholeMap[i][j]=='#')	{
					ImageIcon wallTileIcon = new ImageIcon( "img/wallTile.png" );
					temp = new JLabel(wallTileIcon);
					leftPanel.add(temp, gbc);
				}
				// if tile is an empty tile, add that icon to the appropriate square on the grid
				else if(wholeMap[i][j]=='.'){
					ImageIcon emptyTileIcon = new ImageIcon( "img/emptyTile.png" );
					temp = new JLabel(emptyTileIcon);
					leftPanel.add(temp, gbc);
				}
				// if tile is a player tile, add that icon to the appropriate square on the grid
				else if(wholeMap[i][j]=='P'){
					ImageIcon playerTileIcon = new ImageIcon( "img/playerTile.png" );
					temp = new JLabel(playerTileIcon);
					leftPanel.add(temp, gbc);
				}
				// if tile is a gold tile, add that icon to the appropriate square on the grid
				else if(wholeMap[i][j]=='G'){
					ImageIcon goldTileIcon = new ImageIcon( "img/goldTile.png" );
					temp = new JLabel(goldTileIcon);
					leftPanel.add(temp, gbc);
				}
				// if tile is an exit tile, add that icon to the appropriate square on the grid
				else if(wholeMap[i][j]=='E'){
					ImageIcon exitTileIcon = new ImageIcon( "img/exitTile.png" );
					temp = new JLabel(exitTileIcon);
					leftPanel.add(temp, gbc);
				}
				// if tile is an X tile, add that icon to the appropriate square on the grid
				else if(wholeMap[i][j]=='X'){
					ImageIcon xTileIcon = new ImageIcon( "img/xTile.png" );
					temp = new JLabel(xTileIcon);
					leftPanel.add(temp, gbc);
				}
			}
		}
		
		window.setVisible(true);
		window.repaint();
	}
}