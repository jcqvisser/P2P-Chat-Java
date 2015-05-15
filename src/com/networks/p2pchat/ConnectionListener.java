package com.networks.p2pchat;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Connection listener class, will listen on a particular port for incoming 
 * connections, when one is received it will pass the newly opened socket
 * to the post office class to be handled there.
 * 
 * @author Anthony
 *
 */

public class ConnectionListener implements Runnable {
	/**
	 * Constructor - takes in the holding class and the listening port.
	 * @param postOffice
	 * @param port
	 * @throws IOException
	 */
	public ConnectionListener(PostOffice postOffice, int port) throws IOException {
		_postOffice = postOffice;
		
		// Create the server listen socket object.
		_listenSocket = new ServerSocket(port);
		start();
	}
	
	/**
	 * Run loop for the listen thread, listens for new connections and passes
	 * the sockets of new connections back to post office.
	 */
	public void run() {
		while(_runThread) {
			try {
				_postOffice.addConversation(_listenSocket.accept());
			} catch(IOException ioe) {
				System.err.println("Error creating new socket for incoming connection: " + ioe);
			}
		}
	}
	
	/**
	 * Close the listening socket, and the thread that is running.
	 */
	public void close() {
		_runThread = false;
		try {
			_listenSocket.close();
		} catch (IOException ioe) {
			System.err.println("Error closing the listen socket: " + ioe);
		}
	}
	
	/**
	 * Start the thread.
	 */
	public void start() {
		_runThread = true;
		if (_thread == null)
		{
			_thread = new Thread (this, "ListenThread");
			_thread.start ();
		}
	}
	
	/**
	 * Private member variables.
	 */
	/**
	 * The thread for this class will run as long as run thread is true.
	 */
	private volatile boolean _runThread;
	/**
	 * Instance of the holding class used to pass sockets back when incoming
	 * connections are made.
	 */
	private PostOffice _postOffice;
	/**
	 * The listen socket will listen for incoming connections.
	 */
	private ServerSocket _listenSocket;
	/**
	 * The thread object determines thread operations of the class.
	 */
	private Thread _thread;
}
