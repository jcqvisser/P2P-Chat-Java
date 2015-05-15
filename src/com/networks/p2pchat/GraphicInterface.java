package com.networks.p2pchat;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ListIterator;

public class GraphicInterface {
	public GraphicInterface(PostOffice postOffice) {
		_postOffice = postOffice;
		_me = new Peer();
		_clientWindows = new ArrayList<ClientWindow>();
	}
	
	public void getMyUsername() {
		_loginWindow = new LoginWindow(this);
	}
	
	public synchronized void handleMessage(String message, String ip, String channel) {
		_postOffice.handleMessage(message, ip, channel);
		displayMessage(message, _me, ip, channel);
	}
	
	public void displayMessage(String message, Peer fromWho, String targetIp, String targetChannel) {
		int windowID = findClientWindow(targetIp + targetChannel);
		if(windowID != -1) {
			_clientWindows.get(windowID).displayMessage(message, fromWho);
		} else {
			addWindow(targetIp, targetChannel, message, fromWho);
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
	
	/*
	 * Create a new client window for displaying messages.
	 */
	public void addWindow(String ip, String channel) {
		_clientWindows.add(new ClientWindow(this, ip, channel));
	}
	
	/*
	 * Create a new client window for displaying messages.
	 * Overloaded to take message and display when window is initialized.
	 */
	private void addWindow(String ip, String channel, String message, Peer fromWho) {
		_clientWindows.add(new ClientWindow(this, ip, channel));
		_clientWindows.get(findClientWindow(ip + channel)).displayMessage(message, fromWho);
	}
	
	/*
	 * Remove a client window from the list (and display).
	 */
	public synchronized void removeClientWindow(String ipAndChannel) {
		int windowID = findClientWindow(ipAndChannel);
		if(windowID != -1 && _clientWindows.get(windowID) != null) {
			_clientWindows.get(windowID).dispose();
			_clientWindows.remove(windowID);
		}
	}
	
	/*
	 * Search for a particular client window in the array list of client windows.
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
	private PostOffice _postOffice;
	private ArrayList<ClientWindow> _clientWindows;
	private Peer _me;
	private LoginWindow _loginWindow;
}
