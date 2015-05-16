package com.networks.p2pchat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTextArea;

public class ClientWindow extends JFrame implements Runnable {

	/**
	 * Create the frame.
	 */
	public ClientWindow(GraphicInterface graphicInterface, String ip, String channel) {
		_title = ip + ":" + channel;
		_graphicInterface = graphicInterface;
		_ip = ip;
		_channel = channel;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Handle window closing event.
				_graphicInterface.removeClientWindow(_ip, _channel);
			}
		});

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
		
		start();
	}
	
	
	/**
	 * Display message strings on the screen
	 * @param message
	 * @param fromWho
	 */
	public void displayMessage(String message, Peer fromWho) {
		txtrDisplayArea.append(fromWho.getId() + " (" + fromWho.getIp() + "):- " + message + '\n');
		txtrDisplayArea.setCaretPosition(txtrDisplayArea.getDocument().getLength());
	}
	
	/**
	 * Start the thread that handles the client window.
	 */
	public void start() {
		if (_thread == null)
		{
			_thread = new Thread (this, "ClientWindow:" + _ip);
			_thread.start ();
		}
	}
	
	/**
	 * The thread run loop, is called when the thread starts running.
	 */
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
	 * 	Private data members.
	 */
	/**
	 * The serial for the window.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The JPanel content pane that holds the other input frames.
	 */
	private JPanel contentPane;
	/**
	 * The text field object is used for entering messages from the user.
	 */
	private JTextField textField;
	/**
	 * The graphic interface object is an instance of the containing class,
	 * called when messages are typed from the user and will call this thread 
	 * to display messages.
	 */
	private GraphicInterface _graphicInterface;
	/**
	 * The title object is the title of the window.
	 */
	private String _title;
	/**
	 * The ip object will define the target ip that this client window is for.
	 */
	private String _ip;
	/**
	 * The channel object defines the target channel that this client window is for.
	 * If the channel is set to 'private' it is for direct messaging.
	 */
	private String _channel;
	/**
	 * Text display area displays the chat history with the client.
	 */
	private JTextArea txtrDisplayArea;
	/**
	 * The scroll pane display area allows the chat history to be scrollable.
	 */
	private JScrollPane scrollPaneDisplayArea;
	/**
	 * The thread object handles the threading of the display window.
	 */
	private Thread _thread;
}
