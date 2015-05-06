package com.networks.p2pchat;

import java.io.*;
import java.net.Socket;

public class ServerThreadTCP implements Runnable{
	
	// Public Members:
	public ServerThreadTCP(ConnectionHandleTCP connectionHandler, Socket serverSocket) {
		_serverSocket = serverSocket;
		_clientIP = _serverSocket.getInetAddress().toString();
		_connectionHandler = connectionHandler;
		try {
			_clientInput = new BufferedReader(new InputStreamReader(_serverSocket.getInputStream()));
			_clientOutput = new DataOutputStream(_serverSocket.getOutputStream());
		} catch(IOException e) {
			System.err.println("Error creating stream reader for thread: " + _clientIP);
		}
		start();
	}
	
	public String getIP() {
		return _clientIP;
	}
	
	public void run() {
//		while(!_clientSocket.isClosed()) {
		String clientText = "";
		while(clientText.compareTo("!") !=  0) {
			try {
				clientText = _clientInput.readLine();
				_connectionHandler.serverHandle(_clientIP, clientText);
			} catch( IOException e) {
				System.err.println("Error reading data from socket.");
			}
		}
	}
	
	public void sendMessage(String message) {
		try {
			_clientOutput.writeBytes(message + "\n");
		} catch (IOException ioe) {
			System.err.println("Error sending message to IP: " + _clientIP);
		}
	}
	
	public void close() {
		try {
			_clientInput.close();
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
	private ConnectionHandleTCP _connectionHandler;
}
