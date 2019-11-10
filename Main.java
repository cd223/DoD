import java.awt.*;
import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		Main main = new Main();
		main.buildGUI();
	}
	
	public void buildGUI(){
		// create window frame with default title, its behaviour on close and dimension
				JFrame window = new JFrame("Dungeon of Dooom - Player GUI");
				window.setLocationByPlatform(true);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
				middlePanel.setPreferredSize(new Dimension(350, 350));
				topPanel.setPreferredSize(new Dimension(600, 150));
				leftPanel.setPreferredSize(new Dimension(350, 350));
				rightPanel.setPreferredSize(new Dimension(350, 350));
				
				// place panels in the frame
				window.getContentPane().add(middlePanel, BorderLayout.CENTER);
				window.getContentPane().add(topPanel, BorderLayout.PAGE_START);
				window.getContentPane().add(leftPanel, BorderLayout.LINE_START);
				window.getContentPane().add(rightPanel, BorderLayout.LINE_END);
				
				// change top panel layout to FlowLayout with left alignment
				topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
				middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.PAGE_AXIS));
				
					ImageIcon oldLogo = new ImageIcon("logo.jpg");
					Image img1 = oldLogo.getImage() ;  
					Image newimg1 = img1.getScaledInstance(750, 150,  java.awt.Image.SCALE_SMOOTH ) ;  
					ImageIcon logo = new ImageIcon( newimg1 );
						JLabel logoLabel = new JLabel(logo);
						topPanel.add(logoLabel);
					
					JLabel pic1 = new JLabel(new ImageIcon("menuPic1.png"));
					leftPanel.add(pic1);
					JLabel pic2 = new JLabel(new ImageIcon("menuPic2.png"));
					rightPanel.add(pic2);
					
					JLabel ipAddress = new JLabel();
						ipAddress.setText("IP address:");
						ipAddress.setForeground(Color.WHITE);
						ipAddress.setFont(new Font("Lucida Console", Font.BOLD, 16));
						ipAddress.setPreferredSize(new Dimension(50,25));
						ipAddress.setAlignmentX(Component.CENTER_ALIGNMENT);
					middlePanel.add(ipAddress);
					
					JTextField ipAddressField = new JTextField();
						ipAddressField.setFont(new Font("Lucida Console", Font.BOLD, 16));
						ipAddressField.setEditable(true);
						ipAddressField.setBackground(Color.BLACK);
						ipAddressField.setForeground(Color.WHITE);
						ipAddressField.setPreferredSize(new Dimension(50,25));
						ipAddressField.setAlignmentX(Component.CENTER_ALIGNMENT);
					middlePanel.add(ipAddressField);
					
					JLabel portNumber = new JLabel();
						portNumber.setText("Port number:");
						portNumber.setForeground(Color.WHITE);
						portNumber.setFont(new Font("Lucida Console", Font.BOLD, 16));
						portNumber.setPreferredSize(new Dimension(100,50));
						portNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
					middlePanel.add(portNumber);
						
					JTextField portNumberField = new JTextField();
						portNumberField.setFont(new Font("Lucida Console", Font.BOLD, 16));
						portNumberField.setEditable(true);
						portNumberField.setBackground(Color.BLACK);
						portNumberField.setForeground(Color.WHITE);
						portNumberField.setPreferredSize(new Dimension(100,50));
						portNumberField.setAlignmentX(Component.CENTER_ALIGNMENT);
					middlePanel.add(portNumberField);
					
					// add button to start game
					JButton startGame = new JButton("START");
					startGame.setBackground(new Color(0,175,140));
					startGame.setForeground(Color.WHITE);
					startGame.setFocusPainted(false);
					startGame.setFont(new Font("Lucida Console", Font.BOLD, 16));
						ImageIcon start = new ImageIcon("start.png");
					startGame.setIcon(start);
					startGame.setAlignmentX(Component.CENTER_ALIGNMENT);
					middlePanel.add(startGame);
						
				window.pack();
				window.setVisible(true);
	}

}
