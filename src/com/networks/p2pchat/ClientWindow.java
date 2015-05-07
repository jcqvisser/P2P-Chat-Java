package com.networks.p2pchat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextArea;

public class ClientWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextArea txtrTextarea;
	private ClientThreadTCP _clientHandler;
	private String _title;

	/**
	 * Launch the application.
	 */
	
	public void displayMessage(String message) {
		txtrTextarea.append(message + '\n');
	}

	/**
	 * Create the frame.
	 */
	public ClientWindow(ClientThreadTCP clientHandler, String title) {
		_title = title;
		_clientHandler = clientHandler;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 518, 365);
		setTitle(_title);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setText("");
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					_clientHandler.sendMessage(textField.getText());
					textField.setText("");
				}
			}
		});
		textField.setBounds(10, 285, 482, 30);
		contentPane.add(textField);
		textField.setColumns(10);
		
		txtrTextarea = new JTextArea();
		txtrTextarea.setText("");
		txtrTextarea.setEditable(false);
		txtrTextarea.setBounds(10, 11, 482, 263);
		contentPane.add(txtrTextarea);
	}
}
