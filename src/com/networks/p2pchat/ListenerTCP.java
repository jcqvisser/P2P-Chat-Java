package com.networks.p2pchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ListenerTCP implements Listener, Runnable {
	// Public Members
	
	public ListenerTCP(int port) {
		try {
			_listenSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Error creating TCP socket");
		}
		_checkConnections = true;
		_clientSockets = new ArrayList<ConnectionTCP>();
	}
	
	public void run() {
		while(_checkConnections) {
			try {
				_clientSockets.add(new ConnectionTCP(_listenSocket.accept()));
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
	private ArrayList<ConnectionTCP> _clientSockets;
	private boolean _checkConnections;
}
