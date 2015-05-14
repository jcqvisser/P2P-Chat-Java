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
	private GraphicInterface _graphicInterface;
	private String _title;
	private String _ip;
	private String _channel;
	private JTextArea txtrDisplayArea;
	private JScrollPane scrollPaneDisplayArea;
	
	/**
	 *	Display message strings on the screen
	 */
	
	public void displayMessage(String message, Peer fromWho) {
		txtrDisplayArea.append(fromWho.getId() + "(" + fromWho.getIp() + "): " + message + '\n');
		txtrDisplayArea.setCaretPosition(txtrDisplayArea.getDocument().getLength());
	}
	
	/**
	 * Remove get the IP and Channel String, unique for this chat window.
	 */
	public String getIpChannel() {
		return _ip + _channel;
	}

	/**
	 * Create the frame.
	 */
	public ClientWindow(GraphicInterface graphicInterface, String ip, String channel) {
		_title = ip + ":" + channel;
		_graphicInterface = graphicInterface;
		_ip = ip;
		_channel = channel;
		
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
					_graphicInterface.handleMessage(textField.getText(), _ip, _channel);
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
