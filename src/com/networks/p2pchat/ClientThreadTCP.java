package com.networks.p2pchat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThreadTCP implements Runnable {

	public ClientThreadTCP(Socket clientSocket) {
		_clientSocket = clientSocket;
		_serverIP = _clientSocket.getInetAddress().toString();
		_clientChatHandle = new ClientChatThreadTCP(this, _clientSocket);
		try {
			_inFromUser = new BufferedReader( new InputStreamReader(System.in));
			_serverOutput = new DataOutputStream(_clientSocket.getOutputStream());
		} catch(IOException e) {
			System.err.println("Could not create client side output stream to server.");
		}
		
		start();
	}
	
	public void run() {
		String userInput = "";
		while(userInput.compareTo("!") != 0) {
			try {
				userInput = _inFromUser.readLine();
			} catch (IOException ioe) {
				System.err.println("Couldn't recieve user input in thread: " + _serverIP + " - " + ioe.getMessage());
			}
			try {
				_serverOutput.writeBytes(userInput + '\n');
			} catch (IOException ioe) {
				System.err.println("Couldn't send message to server." + ioe.getMessage());
				break;
			}
		}
		close();
	}
	
	public void messageHandle(String message) {
		if(message.compareTo("!") == 0) {
			_clientChatHandle.close();
		} else {
			System.out.println(message);
		}
	}
	
	public void close() {
		try {
			_clientSocket.close();
		} catch (IOException e) {
			System.err.println("Could not close client socket");
		}
	}
	
	private void start() {
		if (_thread == null)
		{
			System.out.println("Client connected to: " + _serverIP);
			_thread = new Thread (this, _serverIP);
			_thread.start ();
		}
	}
	
	
	// Private members
	
	private Socket _clientSocket;
	private ClientChatThreadTCP _clientChatHandle;
	private Thread _thread;
	private String _serverIP;
	private DataOutputStream _serverOutput;
	private BufferedReader _inFromUser;
	
}
