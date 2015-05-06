package com.networks.p2pchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionHandleTCP implements ConnectionHandle, Runnable {
	// Public Members
	
	public ConnectionHandleTCP(int port) {
		try {
			_listenSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Error creating TCP socket");
		}
		_checkConnections = true;
		_serverSockets = new ArrayList<ServerThreadTCP>();
		_clientSockets = new ArrayList<ClientThreadTCP>();
	}
	
	public void connect(String ipAddr, int port) {
		try {
			Socket clientSocket = new Socket(ipAddr, 1337);
			_clientSockets.add(new ClientThreadTCP(clientSocket));
		} catch(IOException e) {
			System.err.println("Error - Cannot create client socket");
		}
		
	}
	
	public void run() {
		while(_checkConnections) {
			try {
				_serverSockets.add(new ServerThreadTCP(_listenSocket.accept()));
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
	private ArrayList<ServerThreadTCP> _serverSockets;
	private ArrayList<ClientThreadTCP> _clientSockets;
	private boolean _checkConnections;
}
