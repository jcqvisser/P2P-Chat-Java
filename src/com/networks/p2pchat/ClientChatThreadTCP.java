package com.networks.p2pchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientChatThreadTCP implements Runnable {
	public ClientChatThreadTCP(ClientThreadTCP clientHandler, Socket clientSocket) {
		_clientSocket = clientSocket;
		_clientHandler = clientHandler;
		_serverIP = _clientSocket.getInetAddress().toString();
		_serverPort = _clientSocket.getPort();
		try {
			_serverInput = new BufferedReader(new InputStreamReader(_clientSocket.getInputStream()));
		} catch(IOException e) {
			System.err.println("Could not create client side output stream to server.");
		}
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
		String serverText = "";
		while(_runClientChatThread) {
			try {
				serverText = _serverInput.readLine();
				if(serverText != null)
					_clientHandler.messageHandle(serverText);
			} catch (IOException ioe) {
				System.err.println("Couldn't read message from server: " + ioe.getMessage());
				_clientHandler.passClose();
			}
		}
	}
	
	// Private
	
	private Socket _clientSocket;
	private BufferedReader _serverInput;
	private ClientThreadTCP _clientHandler;
	private volatile boolean _runClientChatThread;
	private String _serverIP;
	private int _serverPort;
	private Thread _thread;
}
