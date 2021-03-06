package com.networks.p2pchat;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
		_clientWindows = new HashMap<String, ClientWindow>();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			System.err.println("Error setting interface look and feel: " + e);
		}
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
	public synchronized void sendJOIN(String ip, String channel) {
		if(channel.compareTo("private") == 0) {
			addWindow(ip, channel);
		} else {
			_postOffice.sendJOIN(ip, channel);
		}
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
		addWindow(_me.getIp(), channel);
	}
	
	/**
	 * Update channel list on front end.
	 * @param channels
	 */
	public synchronized void updateOwnedChannels(ArrayList<String> channels) {
		_connectionWindow.updateOwnedChannels(channels);
	}
	
	/**
	 * Remove a channel for the user.
	 * @param channelId
	 */
	public synchronized void removeChannel(String channelId){
		_postOffice.removeChannel(channelId);
		removeClientWindow(_me.getIp(), channelId);
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
	 * window to display it in. The target channel is used for identifying the 
	 * chat window.
	 * @param message
	 * @param fromWho
	 * @param targetIp
	 * @param targetChannel
	 */
	public void displayMessage(String message, Peer fromWho, String targetIp, String targetChannel) {
		if(_clientWindows.containsKey(targetIp + targetChannel)){
			_clientWindows.get(targetIp + targetChannel).displayMessage(message, fromWho);
		} else {
			addWindow(targetIp, targetChannel);
			_clientWindows.get(targetIp + targetChannel).displayMessage(message, fromWho);
		}
	}
	
	/**
	 * Display message takes in the message, who the message is from and the target
	 * window to display it in. This instance does not take in a channel id, therefore
	 * is used to display private conversations.
	 * @param message
	 * @param fromWho
	 * @param targetIp
	 */
	public void displayMessage(String message, Peer fromWho, String targetIp) {
		String targetChannel = "private";
		if(_clientWindows.containsKey(targetIp + targetChannel)){
			_clientWindows.get(targetIp + targetChannel).displayMessage(message, fromWho);
		} else {
			addWindow(targetIp, targetChannel);
			_clientWindows.get(targetIp + targetChannel).displayMessage(message, fromWho);
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

		_connectionWindow = new ConnectionWindow(this);
	}
	
	/**
	 * Create a new client window for displaying messages.
	 * @param ip
	 * @param channel
	 */
	public void addWindow(String ip, String channel) {
		if(!_clientWindows.containsKey(ip + channel)) {
			_clientWindows.put((ip + channel), new ClientWindow(this, ip, channel));
		} else if(_clientWindows.get(ip + channel) == null) {
			_clientWindows.put((ip + channel), new ClientWindow(this, ip, channel));
		}
	}
	
	/**
	 * Add an owned channel to the list,
	 * i.e. add a channel that belongs to the current user so the ip
	 * address associated with it is stored in _me.
	 * @param channel
	 */
	public void addWindow(String channel) {
		if(!_clientWindows.containsKey(_me.getIp() + channel)) {
			_clientWindows.put((_me.getIp() + channel), new ClientWindow(this, _me.getIp(), channel));
		} else if(_clientWindows.get(_me.getIp() + channel) == null) {
			_clientWindows.put((_me.getIp() + channel), new ClientWindow(this, _me.getIp(), channel));
		}
	}
	
	/**
	 * Remove a client window from the list (and display).
	 * @param ipAndChannel
	 */
	public synchronized void removeClientWindow(String ip, String channel) {
		if(_clientWindows.containsKey(ip + channel) && _clientWindows.get(ip + channel) != null) {
			_clientWindows.get(ip + channel).dispose();
			_clientWindows.remove(ip + channel);
		} else if(_clientWindows.containsKey(ip + channel)) {
			_clientWindows.remove(ip + channel);
		}
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
	private Map<String, ClientWindow> _clientWindows;
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
