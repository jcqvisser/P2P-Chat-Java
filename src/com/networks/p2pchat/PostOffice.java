package com.networks.p2pchat;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;

import com.networks.p2pchat.Message.MessageType;

public class PostOffice implements Runnable {
	// Create the postoffice object.
	public PostOffice(int port) throws IOException {
		_conversationHolder = new ConversationHolder(this);
		_connectionListener = new ConnectionListener(this, port);
		_graphicInterface = new GraphicInterface(this);
		_ip = Inet4Address.getLocalHost().getHostAddress().toString();
		
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
		_runThread = true;
		if (_thread == null)
		{
			_thread = new Thread (this, "ListenThread");
			_thread.start ();
		}
	}
	
	public void close() {
		_runThread = false;
		_graphicInterface.close();
		_connectionListener.close();
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
		switch(message.getMessageType()){
		case MSG: 
			handleMSG(message);
			break;
		case FILE:
			break;
		case LISTCH:
			break;
		case NICK:
			break;
		case HELO:
			break;
		case HI:
			break;
		case QUIT:
			break;
		case USERS:
			break;
		case PASS:
			break;
		case MSGCH:
			break;
		case REPEAT: 
			break;
		case CH:
			break;
		case LISTUSERS:
			break;
		case JOIN: 
			break;
		}
	}
	
	void handleMSG(Message message) {
		
	}
	
	// Private member variables:
	private ConversationHolder _conversationHolder;
	private GraphicInterface _graphicInterface;
	private ConnectionListener _connectionListener;
	private String _ip;
	private Thread _thread;
	private volatile boolean _runThread;
	private ArrayList<Message> _inbox;
}
