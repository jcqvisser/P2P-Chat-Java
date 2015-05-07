package com.networks.p2pchat;

import java.io.*;

public class Peer2Peer {
	
	// Public Members:

	public static void main(String[] args) {
		_inFromUser = new BufferedReader( new InputStreamReader(System.in));
		String usrInpt = "";
		System.out.println("Would you like to create a client connection, \'y\' for yes, \'n\' for no.");
		usrInpt = getUserInput();
		if(usrInpt.compareToIgnoreCase("y") == 0) {
			_connectionListener = new ConnectionHandleTCP(1338);
			_connectionListener.start("billeh");
			createClientConnection();
		} else {
			_connectionListener = new ConnectionHandleTCP(1337);
			_connectionListener.start("billeh");
		}
		
		try {
			_inFromUser.close();
		} catch(IOException e) {
			System.out.println("Failed to close stream input");
		}
	}
	
	public static String getUserInput() {
		try {
			return _inFromUser.readLine();
		} catch(IOException e) {
			return null;
		}
	}
	
	public static void createClientConnection() {
		System.out.print("Enter an ip address: ");
		String ipAddr = getUserInput();
		
		_connectionListener.connect(ipAddr);
	}
	
	// Private Members:
	
	private static ConnectionHandle _connectionListener;
	private static BufferedReader _inFromUser;

}
