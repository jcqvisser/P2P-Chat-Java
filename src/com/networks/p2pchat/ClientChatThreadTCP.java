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
		
		try {
			_serverInput = new BufferedReader(new InputStreamReader(_clientSocket.getInputStream()));
		} catch(IOException e) {
			System.err.println("Could not create client side output stream to server.");
		}
		start();
	}
	
	public void start() {
		if (_thread == null)
		{
			_thread = new Thread (this, _serverIP);
			_thread.start ();
		}
	}
	
	public void close() {
		try {
			_serverInput.close();
		} catch (IOException ioe) {
			System.err.println("Error closing client socket read buffer.");
		}
	}
	
	public void run() {
		String serverText = "";
		while(serverText.compareTo("!") != 0) {
			try {
				serverText = _serverInput.readLine();
				_clientHandler.messageHandle(serverText);
			} catch (IOException e) {
				System.err.println("Could send message to server.");
			}
		}
	}
	
	// Private
	
	private Socket _clientSocket;
	private BufferedReader _serverInput;
	private ClientThreadTCP _clientHandler;
	private String _serverIP;
	private Thread _thread;
}
