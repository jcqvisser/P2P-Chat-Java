package com.networks.p2pchat;

public class Peer2Peer {
	
	// Public Members:

	public static void main(String[] args) {
		_connectionListener = new ListenerTCP(1337);
		
		_connectionListener.start("billeh");
	}
	
	// Private Members:
	
	private static Listener _connectionListener;

}
