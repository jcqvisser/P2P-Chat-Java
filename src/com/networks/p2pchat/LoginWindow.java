package com.networks.p2pchat;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 * The login window is used to ask the client for their logon credentials.
 * I.e. The username of the client and the IP address they would like to connect to.
 * @author Anthony
 *
 */
public class LoginWindow extends JFrame implements Runnable  {

	/**
	 * Create the frame.
	 * @param GraphicInterface object, the holding object for this class.
	 */
	public LoginWindow(GraphicInterface graphicInterface) {
		_graphicInterface = graphicInterface;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 504, 114);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Initialize the Username window.
		textField = new JTextField();
		textField.setBounds(120, 11, 358, 24);
		contentPane.add(textField);
		// Listens for when the Enter key is pressed to notify that an
			// Username has been entered.
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					setUsernameConnectionIp();
				}
			}
		});
		textField.setColumns(10);
		
		txtpnUser = new JTextPane();
		StyledDocument doc = txtpnUser.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		txtpnUser.setText("ID:");
		txtpnUser.setEditable(false);
		txtpnUser.setBounds(10, 11, 100, 24);
		contentPane.add(txtpnUser);
		
		// Initialize the Ip address window
		textFieldIP = new JTextField();
		textFieldIP.setBounds(120, 46, 358, 24);
		contentPane.add(textFieldIP);
		// Listens for when the Enter key is pressed to notify that an
			// IP address has been entered.
		textFieldIP.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					setUsernameConnectionIp();
				}
			}
		});
		textFieldIP.setColumns(10);
		
		txtpnIpAddress = new JTextPane();
		StyledDocument docIp = txtpnIpAddress.getStyledDocument();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		docIp.setParagraphAttributes(0, docIp.getLength(), center, false);
		txtpnIpAddress.setText("Connection IP:");
		txtpnIpAddress.setEditable(false);
		txtpnIpAddress.setBounds(10, 46, 100, 24);
		contentPane.add(txtpnIpAddress);
		
		start();
	}
	
	public void start() {
		if (_thread == null)
		{
			_thread = new Thread (this, "LoginWindow");
			_thread.start ();
		}
	}
	
	public void run() {
		try {
			setVisible(true);
			toFront();
			repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Once the user has set the connection ip and username pass them
	 * back to the holding class.
	 */
	private void setUsernameConnectionIp() {
		String ip = textFieldIP.getText();
		String id = textField.getText();
		if(id.compareTo("") != 0 && id != null)
			_graphicInterface.setUsernameConnectionIp(id, ip);
	}
	
	/**
	 * Private data members
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private GraphicInterface _graphicInterface;
	private JTextField textField;
	private JTextPane txtpnUser;
	private JTextField textFieldIP;
	private JTextPane txtpnIpAddress;
	private Thread _thread;
}
