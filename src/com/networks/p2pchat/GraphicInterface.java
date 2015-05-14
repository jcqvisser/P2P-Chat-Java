package com.networks.p2pchat;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.ListIterator;

public class GraphicInterface {
	public GraphicInterface(PostOffice postOffice) {
		_postOffice = postOffice;
		_inFromUser = new BufferedReader( new InputStreamReader(System.in));
		_me = new Peer();
		_clientWindows = new ArrayList<ClientWindow>();
	}
	
	public Peer getMyUsername() {
		System.out.println("Please enter a username: ");
		try {
			String username = _inFromUser.readLine();
			_me = new Peer(username, Inet4Address.getLocalHost().getHostAddress().toString());
			return _me;
		} catch (IOException e) {
			System.err.println("Error reading user input: " + e);
			return null;
		}
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
	
	/*
	 * Create a new client window for displaying messages.
	 */
	public void addWindow(String ip, String channel) {
		GraphicInterface tempThis = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientWindow chatWindow = new ClientWindow(tempThis, ip, channel);
					chatWindow.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							// Handle window closing event.
						}
					});
					chatWindow.setVisible(true);
					_clientWindows.add(chatWindow);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/*
	 * Create a new client window for displaying messages.
	 * Overloaded to take message and display when window is initialized.
	 */
	private void addWindow(String ip, String channel, String message, Peer fromWho) {
		GraphicInterface tempThis = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientWindow chatWindow = new ClientWindow(tempThis, ip, channel);
					chatWindow.setVisible(true);
					chatWindow.displayMessage(message, fromWho);
					_clientWindows.add(chatWindow);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
	private BufferedReader _inFromUser;
	private ArrayList<ClientWindow> _clientWindows;
	private Peer _me;
}
