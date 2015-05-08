package com.networks.p2pchat;

import java.io.*;
import java.net.Socket;

public class ServerThreadTCP implements Runnable{
	
	// Public Members:
	public ServerThreadTCP(ServerHandleTCP serverHandler, Socket serverSocket) {
		_serverSocket = serverSocket;
		_clientIP = _serverSocket.getInetAddress().toString();
		_clientPort = _serverSocket.getPort();
		_serverHandler = serverHandler;
		try {
			_clientInput = new BufferedReader(new InputStreamReader(_serverSocket.getInputStream()));
			_clientOutput = new DataOutputStream(_serverSocket.getOutputStream());
		} catch(IOException ioe) {
			System.err.println("Error creating stream reader for thread: " + _clientIP + " - " + ioe.getMessage());
		}
		_runServerThread = true;
		start();
	}
	
	public String getIPPort() {
		return _clientIP + ":" + Integer.toString(_clientPort);
	}
	
	public void run() {
		String clientText = "";
		while(_runServerThread) {
			try {
				clientText = _clientInput.readLine();
				if(clientText != null) {
					_serverHandler.serverHandle(getIPPort(), clientText);
				} else {
					_serverHandler.closeServerSocket(getIPPort());
				}
				
			} catch( IOException ioe) {
				System.err.println("Error reading data from socket: " + ioe.getMessage());
				_serverHandler.closeServerSocket(getIPPort());
			}
		}
	}
	
	public void sendMessage(String message) {
		try {
			_clientOutput.writeBytes(message + "\n");
		} catch (IOException ioe) {
			System.err.println("Error sending message to IP: " + _clientIP + " - " + ioe.getMessage());
		}
	}
	
	public void close() {
		try {
			System.out.println("Closing server socket for: " + getIPPort());
			_runServerThread = false;
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
			_thread = new Thread (this, getIPPort());
			_thread.start ();
		}
	}
	
	private Socket _serverSocket;
	private String _clientIP;
	private int _clientPort;
	private Thread _thread;
	private volatile boolean _runServerThread;
	private BufferedReader _clientInput;
	private DataOutputStream _clientOutput;
	private ServerHandleTCP _serverHandler;
}
