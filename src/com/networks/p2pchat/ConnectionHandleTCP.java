package com.networks.p2pchat;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandleTCP implements ConnectionHandle, Runnable {
	// Public Members
	// Public constructor, intializes listen server on a particular port.
	public ConnectionHandleTCP(int port) {
		try {
			_listenSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Error creating TCP socket");
		}
		// Thread run loop operational.
		_checkConnections = true;
		// Initialize server and client handler objects.
		_serverHandler = new ServerHandleTCP(this);
		_clientHandler = new ClientHandleTCP(this);
		initializeLoginWindow();
	}
	
	// Return the username of the current user.
	public String getMyUsername() {
		return _myUsername;
	}
	
	// Set the username for a particular object.
	// Warning - only call at start of code, will initialize gui.
	public void setMyUsername(String username){
		_myUsername = username;
		if(_loginWindow != null)
			_loginWindow.dispose();
		initializeWindow();
	}
	
	// Create a client connection to a particular IP/Port/Channel
	public void connect(String ipAddr, int port, String channel) {
		try {
			_clientHandler.connect(new Socket(ipAddr, port), channel);
		} catch(IOException e) {
			System.err.println("Error - Cannot create client socket");
		}
	}
	
	// Listen loop for the connection handler, listens for new connections.
	public void run() {
		while(_checkConnections) {
			try {
				_serverHandler.add(_listenSocket.accept());
			} catch (IOException e) {
				System.err.println("Error connection TCP socket connection.");
			}
		}
	}
	
	// Initialize the listen thread.
	public void start(String threadName) {
		if (_thread == null)
		{
			_thread = new Thread (this, threadName);
			_thread.start ();
		}
	}
	
	// Close the listen thread.
	public void close() {
		_checkConnections = false;
	}
	
	// Private Members
	// Initialize the gui window for normal functionality.
	private void initializeWindow() {
		ConnectionHandleTCP tempThis = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectWindow frame = new ConnectWindow(tempThis);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// Initialize the gui window for logon (Enter username).
	private void initializeLoginWindow() {
		ConnectionHandleTCP connectionHandle = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					_loginWindow = new LoginWindow(connectionHandle);
					_loginWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// Private members.
	private Thread _thread;
	private String _myUsername;
	private ServerSocket _listenSocket;
	private ClientHandleTCP _clientHandler;
	private ServerHandleTCP _serverHandler;
	private boolean _checkConnections;
	private LoginWindow _loginWindow;
}
