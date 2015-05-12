package com.networks.p2pchat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextArea;

public class ClientWindow extends JFrame {

	/**
	 * 	Private data members.
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private ClientThreadTCP _clientHandler;
	private String _title;
	private JTextArea txtrDisplayArea;
	private JScrollPane scrollPaneDisplayArea;
	
	/**
	 *	Display message strings on the screen
	 */
	
	public void displayMessage(String message) {
		txtrDisplayArea.append(message + '\n');
		txtrDisplayArea.setCaretPosition(txtrDisplayArea.getDocument().getLength());
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
		
		scrollPaneDisplayArea = new JScrollPane();
		scrollPaneDisplayArea.setBounds(10, 10, 482, 264);
		contentPane.add(scrollPaneDisplayArea);
		
		txtrDisplayArea = new JTextArea();
		txtrDisplayArea.setText("");
		txtrDisplayArea.setEditable(false);
		scrollPaneDisplayArea.setViewportView(txtrDisplayArea);
	}
}
