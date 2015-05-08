package com.networks.p2pchat;

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
	}
	
	public void connect(String ipAddr, int port) {
		try {
			_clientHandler.connect(new Socket(ipAddr, port));
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
	private Thread _thread;
	private ServerSocket _listenSocket;
	private ClientHandleTCP _clientHandler;
	private ServerHandleTCP _serverHandler;
	private boolean _checkConnections;
}
