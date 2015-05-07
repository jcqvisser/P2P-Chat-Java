package com.networks.p2pchat;

import java.io.*;

public class Peer2Peer {
	
	// Public Members:

	public static void main(String[] args) {
		// Initialize input streams.
		_inFromUser = new BufferedReader( new InputStreamReader(System.in));
		int port = setupServerListener();
		_connectionListener.connect("localhost", port);
		while(true) {
			createClientConnection();
		}
	}
	
	// Get input from the user.
	public static String getUserInput() {
		try {
			return _inFromUser.readLine();
		} catch(IOException e) {
			return null;
		}
	}
	
	public static int setupServerListener() {
		System.out.println("Configure listen server - Enter listen port ");
		int port = Integer.parseInt(getUserInput());
		_connectionListener = new ConnectionHandleTCP(port);
		_connectionListener.start("listenThread");
		return port;
	}
	
	public static void createClientConnection() {
		System.out.println("Enter an ip address for client  connection: ");
		String ipAddr = getUserInput();
		System.out.println("Enter a port for client connection: ");
		int port = Integer.parseInt(getUserInput());
		_connectionListener.connect(ipAddr, port);
	}
	
	// Private Members:
	
	private static ConnectionHandle _connectionListener;
	private static BufferedReader _inFromUser;

}
