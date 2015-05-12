package com.networks.p2pchat;

import java.io.*;
import java.net.Socket;

import javax.xml.bind.JAXBException;

public class ServerThreadTCP implements Runnable{
	
	// Public Members:
	// Constructor, takes the holding class object and the socket for a server.
	public ServerThreadTCP(ServerHandleTCP serverHandler, Socket serverSocket) {
		_serverSocket = serverSocket;
		_clientIP = _serverSocket.getInetAddress().toString();
		_clientPort = _serverSocket.getPort();
		_serverHandler = serverHandler;
		_runServerThread = true;
		_messageService = new MessageService(serverSocket);
		start();
	}
	
	// Return the IP address and port that the socket is connected to, unique
	// for a particular client.
	public String getIPPort() {
		return _clientIP + ":" + Integer.toString(_clientPort);
	}
	
	// Thread run loop for the server object, listens for incoming messages
	// and handles them accordingly (e.g send on to rest of the clients in the channel).
	public void run() {
		Message message;
		while(_runServerThread) {
			try {
				message = _messageService.receiveMessage();
				_serverHandler.serverHandle(getIPPort(), message);
			} catch( IOException ioe) {
				System.err.println("Error reading data from socket: " + ioe.getMessage());
				_serverHandler.closeServerSocket(getIPPort());
			} catch (JAXBException e) {
				System.err.println("Could not parse XML data when reading from client : " + e);
				e.printStackTrace();
			}
		}
	}
	
	// Function that notifies the particular server socket to send a message to the client.
	public void sendMessage(Message message) {
		try {
			_messageService.sendMessage(message);
		} catch (JAXBException e) {
			System.err.println("Error sending XML object: " + e);
		}
	}
	
	// Close the socket and the thread for the server socket object.
	public void close() {
		try {
			System.out.println("Closing server socket for: " + getIPPort());
			_runServerThread = false;
			_serverSocket.close();
		} catch( IOException e) {
			System.err.println("Error closing socket.");
		}
	}
	
	// Private Members:
	// Start the thread loop, called when the constructor is done initializing.
	private void start() {
		if (_thread == null)
		{
			_thread = new Thread (this, getIPPort());
			_thread.start ();
		}
	}
	
	// Private data members.
	private Socket _serverSocket;
	private MessageService _messageService;
	private String _clientIP;
	private int _clientPort;
	private Thread _thread;
	private volatile boolean _runServerThread;
	private ServerHandleTCP _serverHandler;
}
