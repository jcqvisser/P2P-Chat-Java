package com.networks.p2pchat;

import java.io.*;

public class Peer2Peer {
	
	// Public Members:

	public static void main(String[] args) {
		// Initialize input streams.
		_inFromUser = new BufferedReader( new InputStreamReader(System.in));
		setupServerListener();
		_connectionListener.connect("localhost", 1337, "test");
	}
	
	// Get input from the user.
	public static String getUserInput() {
		try {
			return _inFromUser.readLine();
		} catch(IOException e) {
			return null;
		}
	}
	
	public static void setupServerListener() {
		_connectionListener = new ConnectionHandleTCP(1337);
		_connectionListener.start("listenThread");
	}
	
	public static void createClientConnection() {
		System.out.println("Enter an ip address for client  connection: ");
		String ipAddr = getUserInput();
		_connectionListener.connect(ipAddr, 1337, "test");
	}
	
	// Private Members:
	
	private static ConnectionHandle _connectionListener;
	private static BufferedReader _inFromUser;

}
