package com.networks.p2pchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GraphicInterface implements Runnable {
	public GraphicInterface(PostOffice postOffice) {
		_postOffice = postOffice;
		_inFromUser = new BufferedReader( new InputStreamReader(System.in));
		_runThread = true;
		start();
	}
	
	public void run() {
		while(_runThread) {
			try {
				String input = _inFromUser.readLine();
				_postOffice.handleMessage(input, "localhost", "test");
			} catch (IOException ioe) {
				System.err.println("Error reading user input: " + ioe);
			}
		}
	}
	
	public void start() {
		if (_thread == null)
		{
			_thread = new Thread (this, "ListenThread");
			_thread.start ();
		}
	}
	
	public void close() {
		_runThread = false;
	}
	
	public void displayMessage(String message, String targetIp, String targetChannel) {
		System.out.println(message);
	}
	
	// Private member variables.
	private PostOffice _postOffice;
	private Thread _thread;
	private volatile boolean _runThread;
	BufferedReader _inFromUser;
}
