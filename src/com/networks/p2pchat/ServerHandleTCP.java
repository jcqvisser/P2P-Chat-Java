package com.networks.p2pchat;

import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;

public class ServerHandleTCP {
	public ServerHandleTCP(ConnectionHandleTCP connectionHandler) {
		_connectionHandler = connectionHandler;
		_serverSockets = new ArrayList<ServerThreadTCP>();
	}
	
	public void add(Socket serverSocket) {
		_serverSockets.add(new ServerThreadTCP(this, serverSocket));
	}
	
	public synchronized void serverHandle(String IPPort, Message message) {
		System.out.println("Server recieved (" + IPPort + "): " + message.getText());
		if(message.getText().compareTo("QUIT") == 0) {
			closeServerSocket(IPPort);
		} else {
			ListIterator<ServerThreadTCP> itr = _serverSockets.listIterator();
			while(itr.hasNext()) {
				ServerThreadTCP serverSocket= itr.next();
				serverSocket.sendMessage(message);
			}
		}
	}
	
	public synchronized void closeServerSocket(String ipPort) {
		int socketID = findServerThreadID(ipPort);
		if(socketID != -1 && _serverSockets.get(socketID) != null) {
			_serverSockets.get(socketID).close();
			_serverSockets.remove(socketID);
		}
	}
	
	// Private members.
	
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
	
	private ArrayList<ServerThreadTCP> _serverSockets;
	@SuppressWarnings("unused")
	private ConnectionHandleTCP _connectionHandler;
}
