package com.networks.p2pchat;

import java.io.IOException;
import java.net.Socket;

import javax.xml.bind.JAXBException;

public class ClientChatThreadTCP implements Runnable {
	public ClientChatThreadTCP(ClientThreadTCP clientHandler, Socket clientSocket) {
		_clientSocket = clientSocket;
		_clientHandler = clientHandler;
		_serverIP = _clientSocket.getInetAddress().toString();
		_serverPort = _clientSocket.getPort();
		_runClientChatThread = true;
		start();
	}
	
	public void start() {
		if (_thread == null)
		{
			_thread = new Thread (this, _serverIP + ":" + Integer.toString(_serverPort));
			_thread.start ();
		}
	}
	
	public void close() {
		_runClientChatThread = false;
	}
	
	public void run() {
		while(_runClientChatThread) {
			try {
				Message message = new Message(_clientSocket.getInputStream());
				_clientHandler.messageHandle(message);
			} catch (IOException ioe) {
				System.err.println("Couldn't read message from server: " + ioe.getMessage());
				_clientHandler.passClose();
			} catch (JAXBException jaxbe) {
				System.err.println("Could not parse XML: " + jaxbe);
			}
		}
	}
	
	// Private
	
	private Socket _clientSocket;
	private ClientThreadTCP _clientHandler;
	private volatile boolean _runClientChatThread;
	private String _serverIP;
	private int _serverPort;
	private Thread _thread;
}
