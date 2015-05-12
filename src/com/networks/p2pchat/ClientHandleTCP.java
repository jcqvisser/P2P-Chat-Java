package com.networks.p2pchat;

import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;

public class ClientHandleTCP {
	// Constructor, takes in the holding class object.
	public ClientHandleTCP(ConnectionHandleTCP connectionHandler) {
		_connectionHandler = connectionHandler;
		_clientSockets = new ArrayList<ClientThreadTCP>();
	}
	
	// Connect a new client socket, takes the socket and channel and stores the object
	// in a list.
	public void connect(Socket clientSocket, String channel) {
		_clientSockets.add(new ClientThreadTCP(this, clientSocket, channel));
	}
	
	// Close the client socket, will search for the socket that needs to be closed
	// and removes it from the list.
	public synchronized void closeClientSocket(String ipPort) {
		int socketID = findClientThreadID(ipPort);
		if(socketID != -1 && _clientSockets.get(socketID) != null) {
			_clientSockets.get(socketID).close();
			_clientSockets.remove(socketID);
		}
	}
	
	public void handshakeServer(Socket clientSocket, String channel) {
		
	}
	
	// Return the username for the current user.
	public String getMyUsername() {
		return _connectionHandler.getMyUsername();
	}
	
	// Private Members:
	// Search through the list of client sockets for a particular socket.
	// Returns the index of the socket.
	private int findClientThreadID(String ipPort) {
		ListIterator<ClientThreadTCP> itr = _clientSockets.listIterator();
		int index = 0;
		while(itr.hasNext()) {
			index = itr.nextIndex();
			if(itr.next().getClientIPPort().compareTo(ipPort) == 0) {
				return index;
			}
		}
		return -1;
	}
	
	// Private data members.
	private ArrayList<ClientThreadTCP> _clientSockets;
	private ConnectionHandleTCP _connectionHandler;
}
