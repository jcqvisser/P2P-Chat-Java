package com.networks.p2pchat;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The graphic interface class is used for displaying all messages to the user
 * and take any input from the user to sent to others.
 * @author Anthony
 * @author Jacques
 *
 */
public class GraphicInterface {
	/**
	 * The constructor will initialize the ui and will take in a post office object,
	 * this is used for displaying messages from the user.
	 * @param postOffice
	 */
	public GraphicInterface(PostOffice postOffice) {
		_postOffice = postOffice;
		_me = new Peer();
		_clientWindows = new ArrayList<ClientWindow>();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			System.err.println("Error setting interface look and feel: " + e);
		}
		_connectionWindow = new ConnectionWindow(this);
	}
	
	/**
	 * Get my username will initialize the login window object so that the user
	 * can enter their username and target connection ip address.
	 */
	public void getMyUsername() {
		_loginWindow = new LoginWindow(this);
	}
	
	/**
	 * The handle message function is called when the user enters a message into
	 * the client window, this is then passed to post office to be handled.
	 * @param message
	 * @param ip
	 * @param channel
	 */
	public synchronized void handleMessage(String message, String ip, String channel) {
		_postOffice.handleMessage(message, ip, channel);
		displayMessage(message, _me, ip, channel);
	}
	
	/**
	 * Update the list of users for display on the connection window.
	 */
	public synchronized void updateUserList() {
		_connectionWindow.updateUserList();
	}
	
	/**
	 * Send the join command to post office, instructing a new connection to
	 * be made to the target ip (and channel).
	 * @param ip
	 * @param channel
	 */
	public synchronized void sendJoin(String ip, String channel) {
		System.out.println("Connect to " + ip + " - " + channel);
	}
	
	/**
	 * Request a list of channels from a particular target ip address.
	 * @param ip
	 */
	public synchronized void sendLISTCH(String ip) {
		_postOffice.sendLISTCH(ip);
	}
	
	/**
	 * Create a new channel
	 * @param channel
	 */
	public synchronized void createChannel(String channel) {
		_postOffice.createChannel(channel);
	}
	
	/**
	 * Call this function once the list of channels has been received to 
	 * update the list of channels for a particular user.
	 * @param ip
	 * @param channels
	 */
	public synchronized void updateChannelList(String ip, ArrayList<String> channels) {
		_connectionWindow.updateChannelList(ip, channels);
	}
	
	/**
	 * Display message takes in the message, who the message is from and the target
	 * window to display it in.
	 * @param message
	 * @param fromWho
	 * @param targetIp
	 * @param targetChannel
	 */
	public void displayMessage(String message, Peer fromWho, String targetIp, String targetChannel) {
		int windowID = findClientWindow(targetIp + targetChannel);
		if(windowID != -1) {
			_clientWindows.get(windowID).displayMessage(message, fromWho);
		} else {
			addWindow(targetIp, targetChannel);
			_clientWindows.get(findClientWindow(targetIp + targetChannel)).displayMessage(message, fromWho);
		}
	}
	
	/**
	 * Function to set the username and ip acquired from the login window.
	 * Warning: This must only be called by the login window, as it disposes the window.
	 * @param username: The new username for the client.
	 * @param ip: The new ip address to connect to.
	 */
	public void setUsernameConnectionIp(String username, String ip) {
		try {
			_me = new Peer(username, Inet4Address.getLocalHost().getHostAddress().toString());
		} catch (UnknownHostException e) {
			System.err.println("Error getting local host address: " + e);
		}
		_postOffice.initialUsernameConnectionIp(username, ip);
		_loginWindow.dispose();
	}
	
	/**
	 * Create a new client window for displaying messages.
	 * @param ip
	 * @param channel
	 */
	public void addWindow(String ip, String channel) {
		_clientWindows.add(new ClientWindow(this, ip, channel));
	}
	
	/**
	 * Remove a client window from the list (and display).
	 * @param ipAndChannel
	 */
	public synchronized void removeClientWindow(String ipAndChannel) {
		int windowID = findClientWindow(ipAndChannel);
		if(windowID != -1 && _clientWindows.get(windowID) != null) {
			_clientWindows.get(windowID).dispose();
			_clientWindows.remove(windowID);
		}
	}
	
	/**
	 * Search for a particular client window in the array list of client windows.
	 * @param ipAndChannel
	 * @return
	 */
	private int findClientWindow(String ipAndChannel) {
		ListIterator<ClientWindow> itr = _clientWindows.listIterator();
		int index = 0;
		while(itr.hasNext()) {
			index = itr.nextIndex();
			if(itr.next().getIpChannel().compareTo(ipAndChannel) == 0) {
				return index;
			}
		}
		return -1;
	}
	
	// Private member variables.
	/**
	 * The post office object, post office will handle the messages typed into
	 * the interface and will give the interface messages to display.
	 */
	private PostOffice _postOffice;
	/**
	 * List of client windows for different conversations.
	 */
	private ArrayList<ClientWindow> _clientWindows;
	/**
	 * This object defines the current user, it is used to display messages
	 * that the user just typed in the client window.
	 */
	private Peer _me;
	/**
	 * The login window is used to get the users id and initial connection ip.
	 */
	private LoginWindow _loginWindow;
	/**
	 * The connection window object displays the connected clients.
	 */
	private ConnectionWindow _connectionWindow;
}
