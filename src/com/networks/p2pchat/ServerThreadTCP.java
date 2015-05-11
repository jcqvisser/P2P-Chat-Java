package com.networks.p2pchat;

import java.io.*;
import java.net.Socket;

import javax.xml.bind.JAXBException;

public class ServerThreadTCP implements Runnable{
	
	// Public Members:
	public ServerThreadTCP(ServerHandleTCP serverHandler, Socket serverSocket) {
		_serverSocket = serverSocket;
		_clientIP = _serverSocket.getInetAddress().toString();
		_clientPort = _serverSocket.getPort();
		_serverHandler = serverHandler;
		_runServerThread = true;
		start();
	}
	
	public String getIPPort() {
		return _clientIP + ":" + Integer.toString(_clientPort);
	}
	
	public void run() {
		while(_runServerThread) {
			try {
				Message message = new Message(_serverSocket.getInputStream());
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
	
	public void sendMessage(Message message) {
		try {
			message.send(_serverSocket.getOutputStream());
		} catch (IOException ioe) {
			System.err.println("Error sending message to IP: " + _clientIP + " - " + ioe.getMessage());
		} catch (JAXBException e) {
			System.err.println("Error parsing XML object");
		}
	}
	
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
	
	private void start() {
		if (_thread == null)
		{
			_thread = new Thread (this, getIPPort());
			_thread.start ();
		}
	}
	
	private Socket _serverSocket;
	private String _clientIP;
	private int _clientPort;
	private Thread _thread;
	private volatile boolean _runServerThread;
	private ServerHandleTCP _serverHandler;
}
