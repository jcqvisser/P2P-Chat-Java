package com.networks.p2pchat;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;

import com.networks.p2pchat.Message.MessageType;

/**
 * Primary Post office class, will determine where messages must be sent
 * as well as any logic behind particular message types.
 * @author Anthony
 *
 */

public class PostOffice implements Runnable {
	// Create the postoffice object.
	public PostOffice(int port) throws IOException {
		_conversationHolder = new ConversationHolder(this);
		_connectionListener = new ConnectionListener(this, port);
		_graphicInterface = new GraphicInterface(this);
		_ip = Inet4Address.getLocalHost().getHostAddress().toString();
		_id = "testId";
		_addressBook = AddressBook.getInstance();
		_inbox = new ArrayList<Message>();
		
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
			_thread = new Thread (this, "PostOfficeThread");
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
	
	/*This version of the handleMessage Function creates a Message object from the information supplied
	 * by the user and sends it to it's destination by placing it in the inbox.*/
	public synchronized void handleMessage(String message, String targetIp, String targetChannel) {
		// Create new message object.
		synchronized(this) {
			_inbox.add(new Message(
				MessageType.MSG,
				new Peer(_id, _ip),
				new Peer("targetId", targetIp),
				targetChannel,
				message));
			notify();
		}
	}
	
	/* The Messenger Function classifies message in the inbox (incoming or outgoing) by it's type and
	 * calls the appropriate function on it to send it to it's destination*/
	private void messenger(Message message) {
		switch(message.getMessageType()){
		case MSG: 
			handleMSG(message);
			break;
		case FILE:
			handleFILE(message);
			break;
		case LISTCH:
			handleLISTCH(message);
			break;
		case NICK:
			handleNICK(message);
			break;
		case HELO:
			handleHELO(message);	
			break;
		case HI:
			handleHI(message);	
			break;
		case QUIT:
//			handleQUIT(message);	
			break;
		case USERS:
//			handleUSERS(message);	
			break;
		case PASS:
//			handlePASS(message);	
			break;
		case MSGCH:
//			handleMSGCH(message);	
			break;
		case REPEAT: 
//			handleREPEAT(message);	
			break;
		case CH:
//			handleCH(message);	
			break;
		case LISTUSERS:
//			handleLISTUSERS(message);	
			break;
		case JOIN: 
//			handleJOIN(message);	
			break;
		}
	}

	/* handleMSG Function deals with the logic of incoming MSG type Messages.*/
	void handleMSG(Message message) {
		if (message.getDestination().getIp().compareTo(_ip) == 0) {
			if (! _addressBook.addressExists(message.getOrigin().getIp())) {
				_addressBook.addAddress(message.getOrigin());
			}
			if (!_addressBook.addressExists(message.getSource().getIp())) {
				_addressBook.addAddress(message.getSource());
			}
			_graphicInterface.displayMessage(message.getText(), 
					message.getOrigin().getIp(), 
					message.getChannelID());
		} else if (_addressBook.addressExists(message.getDestination().getIp())) {
			_conversationHolder.sendMessage(message);
		} else {
			_addressBook.addAddress(message.getDestination());
			_conversationHolder.sendMessage(message);
		}
	}
	
	void handleFILE(Message message) {
		
	}
	
	void handleLISTCH(Message message) {
		
	}

	/* handleNICK function deals with NICK messages used to change a user's Nickname (ID)*/
	void handleNICK(Message message) {
		if (message.getDestination().getIp().compareTo(_ip) == 0){
			
		} else {
			Peer updatedContact = message.getOrigin();
			updatedContact.setId(message.getText());
			_addressBook.addAddress(updatedContact);
		}
	}
	
	void handleHELO(Message message) {
		
	}
	
	void handleHI(Message message) {
		
	}
	
	// Private member variables:
	private ConversationHolder _conversationHolder;
	private GraphicInterface _graphicInterface;
	private ConnectionListener _connectionListener;
	private String _ip;
	private String _id;
	private Thread _thread;
	private volatile boolean _runThread;
	private ArrayList<Message> _inbox;
	private AddressBook _addressBook;
}
