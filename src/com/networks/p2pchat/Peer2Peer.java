package com.networks.p2pchat;

import java.io.*;

public class Peer2Peer {
	
	// Public Members:

	public static void main(String[] args) {
		// Initialize input streams.
		_inFromUser = new BufferedReader( new InputStreamReader(System.in));
	}
	
	// Get input from the user.
	public static String getUserInput() {
		try {
			return _inFromUser.readLine();
		} catch(IOException e) {
			return null;
		}
	}

	private static BufferedReader _inFromUser;

}
