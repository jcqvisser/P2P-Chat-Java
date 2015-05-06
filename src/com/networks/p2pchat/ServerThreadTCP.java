package com.networks.p2pchat;

import java.io.*;
import java.net.Socket;

public class ServerThreadTCP implements Runnable{
	
	// Public Members:
	public ServerThreadTCP(Socket clientSocket) {
		_serverSocket = clientSocket;
		_clientIP = _serverSocket.getInetAddress().toString();
		try {
			_clientInput = new BufferedReader(new InputStreamReader(_serverSocket.getInputStream()));
			_clientOutput = new DataOutputStream(_serverSocket.getOutputStream());
			_inFromUser = new BufferedReader( new InputStreamReader(System.in));
		} catch(IOException e) {
			System.err.println("Error creating stream reader for thread: " + _clientIP);
		}
		start();
	}
	
	public void run() {
//		while(!_clientSocket.isClosed()) {
		String clientText = "";
		String userText = "";
		while(clientText.compareTo("billeh")!= 0 && userText.compareTo("billeh")!= 0) {
			try {
				clientText = _clientInput.readLine();
				System.out.println("Recieved (" + _clientIP + "): " + clientText);
				userText = _inFromUser.readLine();
				_clientOutput.writeBytes(userText + "\n");
			} catch( IOException e) {
				System.err.println("Error reading data from socket.");
			}
		}
		try {
			_serverSocket.close();
		} catch( IOException e) {
			System.err.println("Error closing server socket.");
		}
	}
	
	public void close() {
		try {
			_serverSocket.close();
		} catch( IOException e) {
			System.err.println("Error closing socket.");
		}
	}
	
	// Private Members:
	
	private void start() {
		if (_thread == null)
		{
			_thread = new Thread (this, _clientIP);
			_thread.start ();
		}
	}
	
	private Socket _serverSocket;
	private String _clientIP;
	private Thread _thread;
	private BufferedReader _clientInput;
	private DataOutputStream _clientOutput;
	private BufferedReader _inFromUser;
}
