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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.*;

/**
 * This class is responsible for launching the server on any specified port.
 * @author cjd47
 */
public class StartServer {

	// declare components as public so that they can be updated from any method
	JFrame window;
	JTextField portNumberField;
	JTextField mapNameField;
	
	/**
	 * This main method is the first method that will start up.
	 * It will build an instance of the StartServer GUI which is responsible for launching the server on a chosen port.
	 * @param args
	 */
	public static void main(String[] args) {
		StartServer startServer = new StartServer();
		startServer.buildGUI();
	}
	
	/**
	 * This method builds the actual start server GUI. It declares and places all components on a common JFrame.
	 */
	public void buildGUI(){
		// create window frame with default title, its behaviour on close and dimension
				window = new JFrame("Dungeon of Dooom - Player GUI");
				window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				window.setLocationByPlatform(true);
				window.setAutoRequestFocus(false);
				window.setAlwaysOnTop(false);
				window.setSize(new Dimension(800, 600));
				
				// declare panels to place in the window frame
				JPanel middlePanel = new JPanel();
				JPanel topPanel = new JPanel();
				JPanel leftPanel = new JPanel();
				JPanel rightPanel = new JPanel();
				
				// set the colours of the panels
				middlePanel.setBackground(Color.darkGray);
				topPanel.setBackground(Color.BLACK);
				leftPanel.setBackground(Color.darkGray);
				rightPanel.setBackground(Color.darkGray);
				
				// set the sizes of the panels
				middlePanel.setPreferredSize(new Dimension(350, 450));
				topPanel.setPreferredSize(new Dimension(600, 150));
				leftPanel.setPreferredSize(new Dimension(350, 450));
				rightPanel.setPreferredSize(new Dimension(350, 450));
				
				// place panels in the frame
				window.getContentPane().add(middlePanel, BorderLayout.CENTER);
				window.getContentPane().add(topPanel, BorderLayout.PAGE_START);
				window.getContentPane().add(leftPanel, BorderLayout.LINE_START);
				window.getContentPane().add(rightPanel, BorderLayout.LINE_END);
				
				// change top panel layout to FlowLayout with left alignment
				topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
				middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.PAGE_AXIS));
				
					ImageIcon oldLogo = new ImageIcon("img/logo.jpg");
					Image img1 = oldLogo.getImage() ;  
					Image newimg1 = img1.getScaledInstance(750, 150,  java.awt.Image.SCALE_SMOOTH ) ;  
					ImageIcon logo = new ImageIcon( newimg1 );
						JLabel logoLabel = new JLabel(logo);
						topPanel.add(logoLabel);
					
				// add game related graphics to the interface to make it aesthetically pleasing
					JLabel pic1 = new JLabel(new ImageIcon("img/menuPic1.png"));
					leftPanel.add(pic1);
					JLabel pic2 = new JLabel(new ImageIcon("img/menuPic2.png"));
					rightPanel.add(pic2);
					
					// create a label for the port number 
					JLabel portNumber = new JLabel();
						portNumber.setText("Port number:");
						portNumber.setForeground(Color.WHITE);
						portNumber.setFont(new Font("Lucida Console", Font.BOLD, 16));
						portNumber.setPreferredSize(new Dimension(100,50));
						portNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
					middlePanel.add(portNumber);
					
					// create a text field for port number entry
					portNumberField = new JTextField();
						portNumberField.setFont(new Font("Lucida Console", Font.BOLD, 16));
						portNumberField.setEditable(true);
						portNumberField.setBackground(Color.BLACK);
						portNumberField.setForeground(new Color(0,255,9));
						portNumberField.setPreferredSize(new Dimension(100,50));
						portNumberField.setAlignmentX(Component.CENTER_ALIGNMENT);
					middlePanel.add(portNumberField);
					
					// create a label for the map choice
					JLabel mapName = new JLabel();
						mapName.setText("Map name:");
						mapName.setForeground(Color.WHITE);
						mapName.setFont(new Font("Lucida Console", Font.BOLD, 16));
						mapName.setPreferredSize(new Dimension(100,50));
						mapName.setAlignmentX(Component.CENTER_ALIGNMENT);
					middlePanel.add(mapName);
					
					// create a text field for map name entry
					mapNameField = new JTextField();
						mapNameField.setFont(new Font("Lucida Console", Font.BOLD, 16));
						mapNameField.setEditable(true);
						mapNameField.setBackground(Color.BLACK);
						mapNameField.setForeground(new Color(0,255,9));
						mapNameField.setPreferredSize(new Dimension(100,50));
						mapNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
					middlePanel.add(mapNameField);
					
					// add button to start the server
					JButton launchServer = new JButton("START SERVER");
					launchServer.setBackground(new Color(26,0,255));
					launchServer.setForeground(Color.WHITE);
					launchServer.setFocusPainted(false);
					launchServer.setFont(new Font("Lucida Console", Font.BOLD, 16));
						ImageIcon humanPlayer = new ImageIcon("img/start.png");
					launchServer.setIcon(humanPlayer);
					launchServer.setAlignmentX(Component.CENTER_ALIGNMENT);
					middlePanel.add(launchServer);
					
					// add action listener to launch server button to start the server on the specified port
					launchServer.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							
							String chosenPort;
							final String chosenMap;
							final int chosenPortNumber;
							
							// validate the entry for the port number
							if(portNumberField!=null && mapNameField!=null){
								chosenPort = portNumberField.getText();
								chosenMap = mapNameField.getText();
								
								// convert the entry to a number and ensure it lies between valid limits
								try{
									chosenPortNumber = Integer.parseInt(chosenPort);
									
									if(chosenPortNumber>0 && chosenPortNumber<65536){
											new Thread(){
												// if between valid limits, create a new instance of DungeonServer, passing in the chosen port
												// creating the new instance will launch a new GameEngineGUI
												public void run(){
													try {
														new DungeonServer(chosenPortNumber, chosenMap);
														window.dispose();
													} catch (UnknownHostException e){
														e.printStackTrace();
													} catch (IOException e) {
														e.printStackTrace();
													}
												}
											}.start();
									}
								}
								catch(NumberFormatException nfe){
									// if the port number is invalid, show an error message
									JOptionPane.showMessageDialog(window, "Port number is an invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
								}
								
							}
							
						}
					});
						
				window.pack();
				window.setVisible(true);
	}

}
