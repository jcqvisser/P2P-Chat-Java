package com.networks.p2pchat;

import java.io.*;

public class Peer2Peer {

	/**
	 * Main function, starts when the program starts and initializes the post office
	 * object to start the listener, etc.
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * Initialize the post office object, it takes care of the rest..
		 */
		try {
			_postOffice = new PostOffice(1337);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private static PostOffice _postOffice;

}
