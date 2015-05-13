package com.networks.p2pchat;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.ListIterator;

public class GraphicInterface {
	public GraphicInterface(PostOffice postOffice) {
		_postOffice = postOffice;
		_inFromUser = new BufferedReader( new InputStreamReader(System.in));
		_clientWindows = new ArrayList<ClientWindow>();
	}
	
	public String getMyUsername() {
		System.out.println("Please enter a username: ");
		try {
			return _inFromUser.readLine();
		} catch (IOException e) {
			System.err.println("Error reading user input: " + e);
			return null;
		}
	}
	
	public synchronized void handleMessage(String message, String ip, String channel) {
		_postOffice.handleMessage(message, ip, channel);
	}
	
	public void displayMessage(String message, String targetIp, String targetChannel) {
		int windowID = findClientWindow(targetIp, targetChannel);
		if(windowID != -1) {
			_clientWindows.get(windowID).displayMessage(message);
		} else {
			addWindow(targetIp, targetChannel);
			_clientWindows.get(findClientWindow(targetIp, targetChannel)).displayMessage(message);
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
	 * Remove a client window from the list (and display).
	 */
	public synchronized void removeClientWindow(String ip, String channel) {
		int windowID = findClientWindow(ip, channel);
		if(windowID != -1 && _clientWindows.get(windowID) != null) {
			_clientWindows.get(windowID).dispose();
			_clientWindows.remove(windowID);
		}
	}
	
	/*
	 * Search for a particular client window in the array list of client windows.
	 */
	private int findClientWindow(String ip, String channel) {
		ListIterator<ClientWindow> itr = _clientWindows.listIterator();
		int index = 0;
		while(itr.hasNext()) {
			index = itr.nextIndex();
			if(itr.next().getIpChannel().compareTo(ip + channel) == 0) {
				return index;
			}
		}
		return -1;
	}
	
	// Private member variables.
	private PostOffice _postOffice;
	BufferedReader _inFromUser;
	ArrayList<ClientWindow> _clientWindows;
}
