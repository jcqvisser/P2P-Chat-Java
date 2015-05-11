package com.networks.p2pchat;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandleTCP implements ConnectionHandle, Runnable {
	// Public Members
	
	public ConnectionHandleTCP(int port) {
		try {
			_listenSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Error creating TCP socket");
		}
		_checkConnections = true;
		_serverHandler = new ServerHandleTCP(this);
		_clientHandler = new ClientHandleTCP(this);
		initializeLoginWindow();
//		
	}
	
	public String getMyUsername() {
		return _myUsername;
	}
	
	public void setMyUsername(String username){
		_myUsername = username;
		_loginWindow.dispose();
		initializeWindow();
	}
	
	public void connect(String ipAddr, int port, String channel) {
		try {
			_clientHandler.connect(new Socket(ipAddr, port), channel);
		} catch(IOException e) {
			System.err.println("Error - Cannot create client socket");
		}
	}
	
	public void run() {
		while(_checkConnections) {
			try {
				_serverHandler.add(_listenSocket.accept());
			} catch (IOException e) {
				System.err.println("Error connection TCP socket connection.");
			}
		}
	}
	
	public void start(String threadName) {
		if (_thread == null)
		{
			_thread = new Thread (this, threadName);
			_thread.start ();
		}
	}
	
	public void close() {
		_checkConnections = false;
	}
	
	// Private Members
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
	
	private Thread _thread;
	private String _myUsername;
	private ServerSocket _listenSocket;
	private ClientHandleTCP _clientHandler;
	private ServerHandleTCP _serverHandler;
	private boolean _checkConnections;
	private LoginWindow _loginWindow;
}
