package com.networks.p2pchat;

import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;

import com.networks.p2pchat.Message.MessageType;

public class ServerHandleTCP {
	// Constructor, takes the holding class object.
	public ServerHandleTCP(ConnectionHandleTCP connectionHandler) {
		_connectionHandler = connectionHandler;
		_serverSockets = new ArrayList<ServerThreadTCP>();
	}
	
	// Add a new server socket to the list when an incoming connection is detected.
	public void add(Socket serverSocket) {
		_serverSockets.add(new ServerThreadTCP(this, serverSocket));
	}
	
	// Will handle a received message as needed.
	// e.g. Will send on a message to other members of a channel.
	public synchronized void serverHandle(String IPPort, Message message) {
		System.out.println("Server recieved (" + IPPort + "): " + message.getText());
		if(message.getMessageType() == MessageType.QUIT) {
			closeServerSocket(IPPort);
		} else {
			ListIterator<ServerThreadTCP> itr = _serverSockets.listIterator();
			while(itr.hasNext()) {
				ServerThreadTCP serverSocket= itr.next();
				serverSocket.sendMessage(message);
			}
		}
	}
	
	// Close the server socket, and remove it from the list of server sockets.
	// Will call the server sockets close function that will close relevent threads.
	public synchronized void closeServerSocket(String ipPort) {
		int socketID = findServerThreadID(ipPort);
		if(socketID != -1 && _serverSockets.get(socketID) != null) {
			_serverSockets.get(socketID).close();
			_serverSockets.remove(socketID);
		}
	}
	
	// Private members.
	
	// Find the server thread related to the unique ip address and port, 
	// returns the index of the thread in the list.
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
	
	// Private data members.
	private ArrayList<ServerThreadTCP> _serverSockets;
	@SuppressWarnings("unused")
	private ConnectionHandleTCP _connectionHandler;
}
