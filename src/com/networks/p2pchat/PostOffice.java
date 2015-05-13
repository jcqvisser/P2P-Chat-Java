package com.networks.p2pchat;

import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;

public class PostOffice implements Runnable {
	// Create the postoffice object.
	public PostOffice() {
		_conversationHolder = new ConversationHolder(this);
		
		start();
	}
	
	public void run() {
		while(_runThread) {
			synchronized(this) {
				try {
					wait();
				} catch (InterruptedException ie) {
					System.out.println("Wait interrupted: " + ie);
				}
				ListIterator<Message> itr = _inbox.listIterator();
				while(itr.hasNext()) {
					messenger(itr.next());
					itr.remove();
				}
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
	
	// Add a new conversation object for a particular socket.
	public synchronized boolean addConversation(Socket connectionSocket) {
		return _conversationHolder.addConversation(connectionSocket);
	}
	
	// Handle the received message.
	public synchronized void handleMessage(Message message) {
		// Add message to inbox.
		synchronized(this) {
			_inbox.add(message);
			notify();
		}
	}
	
	public synchronized void handleMessage(String message, String targetIp, String targetChannel) {
		
	}
	
	// Once a message has been recieved, handle it accordingly.
	private void messenger(Message message) {
		
	}
	
	// Private member variables:
	private ConversationHolder _conversationHolder;
	private Thread _thread;
	private ArrayList<Message> _inbox;
	private volatile boolean _runThread;
	private GraphicInterface _graphicInterface;
}
