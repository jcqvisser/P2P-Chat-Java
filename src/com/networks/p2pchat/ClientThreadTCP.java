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
		try {
			_inFromUser = new BufferedReader( new InputStreamReader(System.in));
			_outToServer = new DataOutputStream(_clientSocket.getOutputStream());
		} catch(IOException e) {
			System.err.println("Could not create client side output stream to server.");
		}
		
		start();
	}
	
	public void run() {
		String userInput = "";
		while(userInput.compareTo("billeh") != 0) {
			try {
				userInput = _inFromUser.readLine();
				_outToServer.writeBytes(userInput + '\n');
			} catch (IOException e) {
				System.err.println("Could send message to server.");
			}
		}
		try {
			_clientSocket.close();
		} catch (IOException e) {
			System.err.println("Could not close client socket");
		}
		
	}
	
	private void start() {
		if (_thread == null)
		{
			_thread = new Thread (this, _serverIP);
			_thread.start ();
		}
	}
	
	
	// Private members
	
	private Socket _clientSocket;
	private Thread _thread;
	private String _serverIP;
	private DataOutputStream _outToServer;
	private BufferedReader _inFromUser;
	
}
