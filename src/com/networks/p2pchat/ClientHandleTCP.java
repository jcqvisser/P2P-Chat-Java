package com.networks.p2pchat;

import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;

public class ClientHandleTCP {

	public ClientHandleTCP(ConnectionHandleTCP connectionHandler) {
		_connectionHandler = connectionHandler;
		_clientSockets = new ArrayList<ClientThreadTCP>();
	}
	
	public void connect(Socket clientSocket) {
		_clientSockets.add(new ClientThreadTCP(this, clientSocket));
	}
	
	public synchronized void closeClientSocket(String ipPort) {
		int socketID = findClientThreadID(ipPort);
		if(socketID != -1 && _clientSockets.get(socketID) != null) {
			_clientSockets.get(socketID).close();
			_clientSockets.remove(socketID);
		}
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
	
	
	private ConnectionHandleTCP _connectionHandler;
	private ArrayList<ClientThreadTCP> _clientSockets;
}
