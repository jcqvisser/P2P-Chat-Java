package com.networks.p2pchat;

import java.io.*;

public class Peer2Peer {
	
	// Public Members:

	public static void main(String[] args) {
		_connectionListener = new ConnectionHandleTCP(1337);
		_connectionListener.start("billeh");
		_inFromUser = new BufferedReader( new InputStreamReader(System.in));
		
		System.out.print("Enter an ip address: ");
		String ipAddr = getUserInput();
		System.out.print("Enter a port number: ");
		String port = getUserInput();
		
		_connectionListener.connect(ipAddr, Integer.parseInt(port));
	}
	
	public static String getUserInput() {
		try {
			return _inFromUser.readLine();
		} catch(IOException e) {
			return null;
		}
		
	}
	
	// Private Members:
	
	private static ConnectionHandle _connectionListener;
	private static BufferedReader _inFromUser;

}
