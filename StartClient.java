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
import java.net.ConnectException;
import java.net.UnknownHostException;

import javax.swing.*;

/**
 * This GUI is used to launch the client (human player or bot). 
 * It includes editable text fields for the IP address and port number allowing any valid combination 
 * of the two to be entered so that a client can connect to any server. 
 * This GUI has two buttons, one launching the human player and the other launching the bot. 
 * @author cjd47
 */
public class StartClient {

	// create window frame with default title, its behaviour on close and dimension
	JFrame window;
	
	// declare fields for the IP and port to be entered
	final JTextField portNumberField = new JTextField();
	final JTextField ipAddressField = new JTextField();
	
	/**
	 * This main method is the first method that will start up.
	 * It will build an instance of the StartClient GUI which is responsible for launching the client on an existing server.
	 * @param args
	 */
	public static void main(String[] args) throws ConnectException {
		StartClient startClient = new StartClient();
		startClient.buildGUI();
	}
	
	/**
	 * This method checks to see if the IP entered is a valid IP address.
	 * A valid IP is one of the form "localhost" or "xxx.xxx.xxx.xxx"
	 * @param chosenIP
	 * @return
	 */
	public boolean ipIsValid(String chosenIP) {
		if(chosenIP.equals("localhost") || validateIPAddress(chosenIP)){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * This method checks whether the IP address satisfies the "xxx.xxx.xxx.xxx" format.
	 * @param ipAddress
	 * @return
	 * @reference http://www.wikihow.com/Validate-an-IP-Address-in-Java
	 */
	public boolean validateIPAddress(String ipAddress){
		String[] tokens = ipAddress.split("\\."); 
		
		if (tokens.length != 4) { 
			return false; 
		} 
		
		// this checks if the tokens are integers between 0-255
		for (String str : tokens) { 
			int i = Integer.parseInt(str); 
			if ((i < 0) || (i > 255)) { 
				return false; 
			} 
		} 
		return true;
	}
	
	/**
	 * This method builds the actual start client GUI. It declares and places all components on a common JFrame.
	 * @throws ConnectException
	 */
	public void buildGUI() throws ConnectException {
		// create window frame with default title, its behaviour on close and dimension
				window = new JFrame("Dungeon of Dooom - Player GUI");
				window.setLocationByPlatform(true);
				window.setAutoRequestFocus(false);
				window.setAlwaysOnTop(false);
				window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
					
					// create a label for the IP address and customise its properties
					JLabel ipAddress = new JLabel();
						ipAddress.setText("IP address:");
						ipAddress.setForeground(Color.WHITE);
						ipAddress.setFont(new Font("Lucida Console", Font.BOLD, 16));
						ipAddress.setPreferredSize(new Dimension(50,25));
						ipAddress.setAlignmentX(Component.CENTER_ALIGNMENT);
					middlePanel.add(ipAddress);
					
					// create a text field for the IP address and customise its properties
						ipAddressField.setFont(new Font("Lucida Console", Font.BOLD, 16));
						ipAddressField.setEditable(true);
						ipAddressField.setBackground(Color.BLACK);
						ipAddressField.setForeground(new Color(0,255,9));
						ipAddressField.setPreferredSize(new Dimension(50,25));
						ipAddressField.setAlignmentX(Component.CENTER_ALIGNMENT);
					middlePanel.add(ipAddressField);
					
					// create a label for the port number and customise its properties
					JLabel portNumber = new JLabel();
						portNumber.setText("Port number:");
						portNumber.setForeground(Color.WHITE);
						portNumber.setFont(new Font("Lucida Console", Font.BOLD, 16));
						portNumber.setPreferredSize(new Dimension(100,50));
						portNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
					middlePanel.add(portNumber);
						
					// create a text field for the port number and customise its properties
						portNumberField.setFont(new Font("Lucida Console", Font.BOLD, 16));
						portNumberField.setEditable(true);
						portNumberField.setBackground(Color.BLACK);
						portNumberField.setForeground(new Color(0,255,9));
						portNumberField.setPreferredSize(new Dimension(100,50));
						portNumberField.setAlignmentX(Component.CENTER_ALIGNMENT);
					middlePanel.add(portNumberField);
					
					// add button to start game as a human player
					JButton startHuman = new JButton("LAUNCH HUMAN");
					startHuman.setBackground(new Color(0,175,140));
					startHuman.setForeground(Color.WHITE);
					startHuman.setFocusPainted(false);
					startHuman.setFont(new Font("Lucida Console", Font.BOLD, 16));
						ImageIcon humanPlayer = new ImageIcon("img/launchHuman.png");
					startHuman.setIcon(humanPlayer);
					startHuman.setAlignmentX(Component.CENTER_ALIGNMENT);
					middlePanel.add(startHuman);
					
					// add button to start game as a bot player
					JButton startBot = new JButton("LAUNCH BOT");
					startBot.setBackground(new Color(0,175,140));
					startBot.setForeground(Color.WHITE);
					startBot.setFocusPainted(false);
					startBot.setFont(new Font("Lucida Console", Font.BOLD, 16));
						ImageIcon botPlayer = new ImageIcon("img/launchBot.png");
					startBot.setIcon(botPlayer);
					startBot.setAlignmentX(Component.CENTER_ALIGNMENT);
					middlePanel.add(startBot);
					
					// add action listener to launch human button
					startHuman.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String chosenPort;
							final String chosenIPAddress;
							final int chosenPortNumber;
							
							// validate the entry for the port number and IP address
							if(portNumberField!=null && ipAddressField!=null){
								chosenPort = portNumberField.getText();
								chosenIPAddress = ipAddressField.getText();
								
								// convert the entry to a number and ensure it lies between valid limits
								try{
									chosenPortNumber = Integer.parseInt(chosenPort);
									
									if(chosenPortNumber>0 && chosenPortNumber<65536 && ipIsValid(chosenIPAddress)){
											new Thread(){
												public void run(){
													try {
														// if between valid limits, create a new instance of PlayGame, passing in the chosen port number and IP address
														// creating the new instance will launch a new PlayerGUI in human mode
														new PlayGame(chosenPortNumber, chosenIPAddress, 0);
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
					
					// add action listener to launch bot button
					startBot.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String chosenPort;
							final String chosenIPAddress;
							final int chosenPortNumber;
							
							// validate the entry for the port number and IP address
							if(portNumberField!=null && ipAddressField!=null){
								chosenPort = portNumberField.getText();
								chosenIPAddress = ipAddressField.getText();
								
								// convert the entry to a number and ensure it lies between valid limits
								try{
									chosenPortNumber = Integer.parseInt(chosenPort);
									
									if(chosenPortNumber>0 && chosenPortNumber<65536 && ipIsValid(chosenIPAddress)){
											new Thread(){
												public void run(){
													// if between valid limits, create a new instance of Bot, passing in the chosen port number and IP address
													// creating the new instance will launch a new PlayerGUI in bot mode
													try {
														new Bot(chosenPortNumber, chosenIPAddress, 1);
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
