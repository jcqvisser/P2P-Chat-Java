package com.networks.p2pchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;

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
			_clientSockets.add(new ClientThreadTCP(new Socket(ipAddr, port)));
		} catch(IOException e) {
			System.err.println("Error - Cannot create client socket");
		}
	}
	
	public void run() {
		while(_checkConnections) {
			try {
				_serverSockets.add(new ServerThreadTCP(this, _listenSocket.accept()));
			} catch (IOException e) {
				System.err.println("Error connection TCP socket connection.");
			}
		}
	}
	
	public synchronized void serverHandle(String IPPort, String message) {
		System.out.println("Server recieved (" + IPPort + "): " + message);
		if(message.compareTo("!") == 0) {
			closeSocket(IPPort);
		} else {
			ListIterator<ServerThreadTCP> itr = _serverSockets.listIterator();
			while(itr.hasNext()) {
				ServerThreadTCP serverSocket= itr.next();
				if(serverSocket.getIPPort().compareTo(IPPort) != 0) {
					serverSocket.sendMessage(IPPort + ": " + message);
				}
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
	
	public synchronized void closeSocket(String ipPort) {
		int socketIP = findServerThreadID(ipPort);
		_serverSockets.get(socketIP).close();
		_serverSockets.remove(socketIP);
	}
	
	// Private Members
	
	private int findServerThreadID(String ipPort) {
		ListIterator<ServerThreadTCP> itr = _serverSockets.listIterator();
		int index = 0;
		while(itr.hasNext()) {
			index = itr.nextIndex();
			if(itr.next().getIPPort().compareTo(ipPort) == 0) {
				return index;
			}
		}
		return -1;
	}
	
	private Thread _thread;
	private ServerSocket _listenSocket;
	private ArrayList<ServerThreadTCP> _serverSockets;
	private ArrayList<ClientThreadTCP> _clientSockets;
	private boolean _checkConnections;
}
