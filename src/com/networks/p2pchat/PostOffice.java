package com.networks.p2pchat;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import com.networks.p2pchat.Message.MessageType;

/**
 * Primary Post office class, will determine where messages must be sent
 * as well as any logic behind particular message types.
 * @author Anthony
 * @author Jacques
 *
 */

public class PostOffice implements Runnable {
	/**
	 * The post office constructor will create the subclasses,
	 * and will start the gui login window to get user credentials.
	 * @param port
	 * @throws IOException
	 */
	public PostOffice(int port) throws IOException {
		_port = port;
		_graphicInterface = new GraphicInterface(this);
		_conversationHolder = new ConversationHolder(this, _port);
		_connectionListener = new ConnectionListener(this, _port);
		setMePeer("Unknown");
		
		// Initialize user id.
		_graphicInterface.getMyUsername();
		
		_addressBook = AddressBook.getInstance();
		_inbox = new ArrayList<Message>();
		_heloMessages = new ArrayList<Message>();
		
		start();
	}
	
	/**
	 * Thread run loop, this function overloads the implementation
	 * from Runnable.
	 */
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
	
	/**
	 * Initialize the thread for the post office.
	 */
	public void start() {
		_runThread = true;
		if (_thread == null)
		{
			_thread = new Thread (this, "PostOfficeThread");
			_thread.start ();
		}
	}
	
	/**
	 * Close the thread for the post office object.
	 */
	public void close() {
		_runThread = false;
		_connectionListener.close();
	}
	
	/**
	 * Set the username and connection IP for the host, the program will hold while
	 * waiting for the username and connection ip to continue with the code.
	 * Should only be called by the graphic interface class.
	 * @param username
	 * @param ip
	 */
	public synchronized void initialUsernameConnectionIp(String username, String ip) {
		setMePeer(username);
		if(ip.compareTo("") != 0 && ip != null) {
			try {
				addConversation(new Socket(ip, _port));
			} catch (UnknownHostException e) {
				System.err.println("Error, target host is unknown: " + e);
			} catch (IOException e) {
				System.err.println("Error creating new client connection: " + e);
			}
			_graphicInterface.addWindow(ip, "test");
		}
	}
	
	/**
	 * Add a new conversation object for a particular socket.
	 * @param connectionSocket
	 * @return
	 */
	public synchronized boolean addConversation(Socket connectionSocket) {
		return _conversationHolder.addConversation(connectionSocket);
	}
	
	/**
	 * Handle the received message. This will add the message to the list of messages and
	 * will notify the thread that a message is ready to be handled. 
	 */
	public synchronized void handleMessage(Message message) {
		// Add message to inbox.
		synchronized(this) {
			_inbox.add(message);
			notify();
		}
	}
	
	/**
	 * This version of the handleMessage Function creates a Message object from the information supplied
	 * by the user and sends it to it's destination by placing it in the inbox.
	 * @param message The message object to be forwarded
	 * @param targetIp The IP the message should be sent to.
	 * @param targetChannel The channel that the message should be sent to.
	 */
	public synchronized void handleMessage(String message, String targetIp, String targetChannel) {
		// Create new message object.
		synchronized(this) {
			_inbox.add(new Message(
				MessageType.MSG,
				_me,
				new Peer("targetId", targetIp),
				targetChannel,
				message));
			notify();
		}
	}
	
	/**
	 * Set the peer object that contains information about 'me'
	 * i.e. the client.
	 * @param username
	 */
	private void setMePeer(String username) {
		try {
			_me = new Peer(username, Inet4Address.getLocalHost().getHostAddress().toString());
		} catch (UnknownHostException e) {
			System.err.println("Error getting the host address: " + e);
		}
	}
	
	/**
	 * The Messenger Function classifies message in the inbox (incoming or outgoing) by it's type and
	 * calls the appropriate function on it to send it to it's destination
	 * @param message
	 */
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
			handleMSGCH(message);	
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

	/**
	 * handleMSG Function deals with the logic of incoming MSG type Messages
	 * @param message
	 */
	void handleMSG(Message message) {
		if (message.getDestination().getIp().compareTo(_me.getIp()) == 0) {
			if (! _addressBook.addressExists(message.getOrigin().getIp())) {
				_addressBook.addAddress(message.getOrigin());
			}
			_graphicInterface.displayMessage(message.getText(), 
					message.getOrigin(),
					message.getOrigin().getIp(), 
					message.getChannelID());
		} else if (_addressBook.addressExists(message.getDestination().getIp())) {
			_conversationHolder.sendMessage(message);
		} else {
			_addressBook.addAddress(message.getDestination());
			_conversationHolder.sendMessage(message);
		}
		// TODO pass to GUI
	}
	
	void handleFILE(Message message) {
		
	}
	
	void handleLISTCH(Message message) {
		
	}

	/**
	 * handleNICK function deals with NICK messages used to change a user's 
	 * Nickname (ID)
	 * @param message
	 */
	void handleNICK(Message message) {
		if (message.getDestination().getIp().compareTo(_me.getIp()) == 0){
			
		} else {
			Peer updatedContact = message.getOrigin();
			updatedContact.setId(message.getText());
			_addressBook.addAddress(updatedContact);
		}
	}

	/** 
	 * handleHELO function deals with messages of type HELO,
	 * HELO messages have an Origin, Source and Destination (Peer objects)
	 * When a HELO message is received, source is changed to you (PostOffice._me)
	 * then it is sent on to all your contacts after decrementing the message's
	 * ttl. 
	 * 
	 * If the message's ttl is at 0 it is not sent on.
	 * 
	 * Whenever a HELO is received, the origin is saved in your addressbook,
	 * also a HI message is sent back to the Source (not origin).
	 */
	void handleHELO(Message message) {
		Message messageFwd = new Message(message);
		addToHeloLog(message);
		if (message.getTtl() > 0){
			messageFwd.setSource(_me);
			for (Map.Entry<String, String> entry : _addressBook.getMap().entrySet()) {
				messageFwd.setDestination(new Peer(entry.getValue(), entry.getKey()));
				messageFwd.setTtl(message.getTtl() - 1);
				_conversationHolder.sendMessage(messageFwd);
			}
			if (!_addressBook.addressExists(message.getOrigin().getIp())) {
				_addressBook.addAddress(message.getOrigin());
			}	
		}
		Message messageHI = new Message(MessageType.HI,
						_messageTtl - message.getTtl(),
						_me,
						_me,
						message.getOrigin());
		_conversationHolder.sendMessage(messageHI);
	}
	
	void handleHI(Message message) {
		_addressBook.addAddress(message.getOrigin());
		_addressBook.addAddress(message.getSource());
		
		if (message.getDestination().getIp().compareTo(_me.getIp()) != 0) {
			Message messageFwd = new Message(message);
			messageFwd.setSource(_me);
			Peer newDestination = getHeloSource(message);
			if (newDestination == null) {return;}
			messageFwd.setDestination(newDestination);
			_conversationHolder.sendMessage(messageFwd);
		}
	}
	
	private Peer getHeloSource(Message message) {
		String heloOriginIp = message.getDestination().getIp(); 
		for (Message msg : _heloMessages) {
			if (msg.getOrigin().getIp().compareTo(heloOriginIp) == 0){
				return msg.getSource();
			}
		}
		return null;
	}
	
	private void addToHeloLog(Message message) {
		for (Message msg : _heloMessages) {
			if (msg.getOrigin().getIp().compareTo(message.getOrigin().getIp()) == 0) {
				_heloMessages.remove(msg);
			}
		}
	}
	
	private void handleMSGCH(Message message) {
		// TODO check if channel exists
		
		// TODO check if person is listed in the channel
		// TODO forward message to all channel participants 
	}
	
	/**
	 * Private member variables:
	 */
	/**
	 * The conversation holder object, contains all connected conversations
	 */
	private ConversationHolder _conversationHolder;
	/**
	 * The graphic interface object is used to handle all display functions
	 */
	private GraphicInterface _graphicInterface;
	/**
	 * The connection listener object will initialize a server socket that will
	 * listen for incoming connections.
	 */
	private ConnectionListener _connectionListener;
	/**
	 * Contains information about the user ('me').
	 */
	private Peer _me;
	/**
	 * The thread object is used to handle the threading of the post office class.
	 */
	private Thread _thread;
	/**
	 * The post office thread will run as long as this is true.
	 */
	private volatile boolean _runThread;
	/**
	 * The inbox list contains all messages that are waiting to be handled by the
	 * post office object.
	 */
	private ArrayList<Message> _inbox;
	/**
	 * The addressbook contains the usernames and ip addresses of all known people
	 * connected to the network.
	 */
	private AddressBook _addressBook;
	/**
	 * The message time to live defines how long a message will travel in the network
	 * before it is deleted.
	 */
	private int _messageTtl = 4;
	/**
	 * This list contains all of the helo messages that have been received to avoid
	 * resending a message to someone who has already received one.
	 */
	private ArrayList<Message> _heloMessages;
	/** 
	 * The port object defines the port that the program is listening on,
	 * and therefore the port that connections are made to.
	 */
	private int _port;
	/*
	 * The channellist object is a collection of all the channels that this user
	 * hosts. keyed by their name and ip
	 */
	private HashMap<String, Channel> _channelList;
}
