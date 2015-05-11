package com.networks.p2pchat;

import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;

public class ClientHandleTCP {

	public ClientHandleTCP(ConnectionHandleTCP connectionHandler) {
		_connectionHandler = connectionHandler;
		_clientSockets = new ArrayList<ClientThreadTCP>();
	}
	
	public void connect(Socket clientSocket, String channel) {
		_clientSockets.add(new ClientThreadTCP(this, clientSocket, channel));
	}
	
	public synchronized void closeClientSocket(String ipPort) {
		int socketID = findClientThreadID(ipPort);
		if(socketID != -1 && _clientSockets.get(socketID) != null) {
			_clientSockets.get(socketID).close();
			_clientSockets.remove(socketID);
		}
	}
	
	public void handshakeServer(Socket clientSocket, String channel) {
		
	}
	
	public String getMyUsername() {
		return _connectionHandler.getMyUsername();
	}
	
	// Private Members:
	
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
	
	
	private ArrayList<ClientThreadTCP> _clientSockets;
	private ConnectionHandleTCP _connectionHandler;
}
