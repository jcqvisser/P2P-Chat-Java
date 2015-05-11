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

public class LoginWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ConnectionHandleTCP _connectionHandler;
	private JTextField textField;
	JTextPane txtpnIpAddress;

	/**
	 * Create the frame.
	 */
	public LoginWindow(ConnectionHandleTCP connectionHandler) {
		_connectionHandler = connectionHandler;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 504, 86);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(120, 11, 358, 24);
		contentPane.add(textField);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					_connectionHandler.setMyUsername(textField.getText());
				}
			}
		});
		textField.setColumns(10);
		
		txtpnIpAddress = new JTextPane();
		StyledDocument doc = txtpnIpAddress.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		txtpnIpAddress.setText("ID:");
		txtpnIpAddress.setEditable(false);
		txtpnIpAddress.setBounds(10, 11, 100, 24);
		contentPane.add(txtpnIpAddress);
	}
}
