package com.networks.p2pchat;

import java.io.*;
import java.net.Socket;

public class ConnectionTCP implements Runnable{
	
	// Public Members:
	public ConnectionTCP(Socket clientSocket) {
		_clientSocket = clientSocket;
		_clientIP = _clientSocket.getInetAddress().toString();
		try {
			_clientInput = new BufferedReader(new InputStreamReader(_clientSocket.getInputStream()));
		} catch(IOException e) {
			System.err.println("Error creating stream reader for thread: " + _clientIP);
		}
		start();
	}
	
	public void run() {
//		while(!_clientSocket.isClosed()) {
		String clientText = "";
		while(clientText.compareTo("billeh")!= 0) {
			try {
				clientText = _clientInput.readLine();
				System.out.println("Recieved: " + clientText);
			} catch( IOException e) {
				System.err.println("Error reading data from socket.");
			}
		}
	}
	
	public void close() {
		try {
			_clientSocket.close();
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
	
	private Socket _clientSocket;
	private String _clientIP;
	private Thread _thread;
	private BufferedReader _clientInput;
}
