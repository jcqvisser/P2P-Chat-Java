package com.networks.p2pchat;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import com.networks.p2pchat.Channel.JoinResponse;
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
		
		_addressBook = AddressBook.getInstance();
		_inbox = new ArrayList<Message>();
		_channelList = new HashMap<String, Channel>();
		_heloMessages = new ArrayList<Message>();
		_messageBuilder = new MessageBuilder(_conversationHolder, _me);
		
		// Initialize user id.
		setMePeer("Unknown");
		_graphicInterface.getMyUsername();
		
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
			_messageBuilder.sendHELO(ip);
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
			if (targetChannel.compareTo("private") == 0){
				_inbox.add(new Message(
						MessageType.MSG,
						_me,
						_addressBook.getAddress(targetIp),
						//new Peer("targetId", targetIp),
						targetChannel,
						message));
			} else if(targetIp.compareTo(_me.getIp()) == 0) {
				_inbox.add(new Message(
						MessageType.MSGCH,
						_me,
						_me,
						targetChannel,
						message));
			}
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
			_messageBuilder.updateMePeer(_me);
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
			handleQUIT(message);	
			break;
		case USERS:
			handleUSERS(message);	
			break;
		case PASS:
			handlePASS(message);	
			break;
		case MSGCH:
			handleMSGCH(message);	
			break;
		case REPEAT: 
//			handleREPEAT(message);	
			break;
		case CH:
			handleCH(message);	
			break;
		case LISTUSERS:
			handleLISTUSERS(message);	
			break;
		case JOIN: 
			handleJOIN(message);	
			break;
		case INVALIDPASS:
//			handleINVALIDPASS(message);
// send to gui
			break;
		case NOTAMEMBER:
//			handleNOTAMEMBER(message);
// send to gui
			break;
		case JOINED:
			handleJOINED(message);
			break;
		case INVALIDCH:
//			handleINVALIDCH(message);
		}
	}

	/**
	 * handleMSG Function deals with the logic of incoming MSG type Messages
	 * @param message
	 */
	void handleMSG(Message message) {
		if (message.getDestination().getIp().compareTo(_me.getIp()) == 0) {
			if (! _addressBook.addressExists(message.getOrigin().getIp())) {
				addAddress(message.getOrigin());
			}
			_graphicInterface.displayMessage(message.getText(), 
					message.getOrigin(),
					message.getOrigin().getIp(), 
					"private");
		} else if (_addressBook.addressExists(message.getDestination().getIp())) {
			_conversationHolder.sendMessage(message);
		} else {
			addAddress(message.getDestination());
			_conversationHolder.sendMessage(message);
		}
	}
	
	void handleFILE(Message message) {
		
	}
	
	void handleLISTCH(Message message) {
		ArrayList<String> channelIDs= new ArrayList<String>();
		for (String key : _channelList.keySet()) {
			channelIDs.add(key);
		}	
		Message messageCH = new Message(MessageType.CH,
									_me,
									message.getOrigin(),
									channelIDs);
		_conversationHolder.sendMessage(messageCH);
	}
	
	void handleCH(Message message) {
		if (message.getMessageType() == MessageType.CH){
			_graphicInterface.updateChannelList(message.getOrigin().getIp(), message.getChannelList());
		}
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
			addAddress(updatedContact);
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
	 * also a HI message is sent back to the Source (not origin).A
	 * @see com.networks.p2pchat.PostOffice#addToHeloLog(Message)
	 */
	void handleHELO(Message message) {
		addAddress(message.getSource());
		addAddress(message.getOrigin());
		Message messageFwd = new Message(message);
		addToHeloLog(message);
		if (message.getTtl() > 0){
			messageFwd.setSource(_me);
			for (Map.Entry<String, String> entry : _addressBook.getMap().entrySet()) {
				messageFwd.setDestination(new Peer(entry.getValue(), entry.getKey()));
				messageFwd.setTtl(message.getTtl() - 1);
				_conversationHolder.sendMessage(messageFwd);
			}
		}
		Message messageHI = new Message(MessageType.HI,
						_messageTtl - message.getTtl(),
						_me,
						_me,
						message.getOrigin());
		_conversationHolder.sendMessage(messageHI);
	}

	/**
	 * handleHI function is called to deal with messages of type {@linkplain MessageType#HI} 
	 * 
	 * HI messages are sent back to the source of the corresponding {@link MessageType#HELO} 
	 * using the logic in this function. HELO messages are stored and their origin is compared to the
	 * destination of HI messages, the HI message is then forwarded to the source of the HELO message.
	 * @param message is a {@link Message} object of type {@link MessageType#HI}
	 * @see com.networks.p2pchat.PostOffice#getHeloSource(Message)
	 */
	void handleHI(Message message) {
		addAddress(message.getOrigin());
		addAddress(message.getSource());
		if (message.getDestination().getIp().compareTo(_me.getIp()) != 0) {
			Message messageFwd = new Message(message);
			messageFwd.setSource(_me);
			Peer newDestination = getHeloSource(message);
			if (newDestination == null) {return;}
			messageFwd.setDestination(newDestination);
			_conversationHolder.sendMessage(messageFwd);
		}
	}
	
	/**
	 * the getHeloSource function finds the {@link MessageType#HELO} message correspnding
	 * to the HI message passed in in order to let the HI message find it's path back to it's origin.
	 * @see com.networks.p2pchat.PostOffice#handleHI(Message)
	 * @param message a HI message of type {@linkplain com.networks.p2pchat.Message}
	 * @return A Peer object that is the source of the HELO message corresponding the the HI message parameter 
	 */
	private Peer getHeloSource(Message message) {
		String heloOriginIp = message.getDestination().getIp(); 
		for (Message msg : _heloMessages) {
			if (msg.getOrigin().getIp().compareTo(heloOriginIp) == 0){
				return msg.getSource();
			}
		}
		return null;
	}

	/**
	 * Adds a {@link com.networks.p2pchat.Message} object of type {@link com.networks.p2pchat.Message.MessageType#HELO} to the local list thereof
	 * so its source and origin may be used to determine the forward path of subsequent {@link com.networks.p2pchat.Message.MessageType#HI} messages
	 * @param message a Message object of type HELO
	 */
	private void addToHeloLog(Message message) {
		for (Message msg : _heloMessages) {
			if (msg.getOrigin().getIp().compareTo(message.getOrigin().getIp()) == 0) {
				_heloMessages.remove(msg);
			}
		}
	}
	
	private void handleMSGCH(Message message) {
		if (!channelExists(message.getChannelID())) {
			// TODO channel doesn't exist message
			return; 
		}
		
		if (!_channelList.get(message.getChannelID()).hasUser(message.getOrigin())) {
			// TODO user is not part of channel message
			return;
		}
		
		for (Map.Entry<String, String> entry : _channelList.get(message.getChannelID()).getUsers().entrySet()) {
			if (entry.getKey().compareTo(_me.getIp()) != 0){
				Message messageFwd = new Message(message);
				messageFwd.setDestination(new Peer(entry.getValue(), entry.getKey()));
				messageFwd.setSource(_me);
				_conversationHolder.sendMessage(messageFwd);
			}
		}
		_graphicInterface.displayMessage(message.getText(), 
				message.getOrigin(), 
				message.getDestination().getIp(), 
				message.getChannelID());
	}
/**
 * Determines whether a channel exists in the {@link _channelList} as defined by it's
 * name	
 * @param channelID: A String
 * @return
 */
	private boolean channelExists(String channelID) {
		for (String key : _channelList.keySet()) {
			if (channelID.compareTo(key) == 0) {
				return true;
			}
		}
		return false;
	}
	
/**
 * Handles a {@link MessageType#QUIT} by removing the user from the channel specified
 * in {@link Message#getChannelID()} if that channel is owned by the user the QUIT message
 * is sent to.
 * @param message
 */
	private void handleQUIT(Message message) {
		if (message.getMessageType() != MessageType.QUIT) {
			return;
		}
		if (!channelExists(message.getChannelID())) {
			// TODO channel doesn't exist message
			return;
		}
		
		_channelList.get(message.getChannelID()).removeUser(message.getOrigin());
		// TODO send to gui
		// TODO send userhasquit message to channel users
	}
	
	private void handleLISTUSERS(Message message) {
		if (message.getMessageType() != MessageType.USERS) {
			return;
		}
		if (!channelExists(message.getChannelID())) {
			// TODO channel doesn't exist message  
			return;
		}	
		
		ArrayList<Peer> users = new ArrayList<Peer>();
		for (Map.Entry<String,String> entry : _channelList.get(message.getChannelID()).getUsers().entrySet()) {
			users.add(new Peer(entry.getValue(), entry.getKey()));
		}
		
		Message messageReply = new Message(MessageType.USERS,
				_me,
				users,
				message.getOrigin());
		
		_conversationHolder.sendMessage(messageReply);
	}
	
	private void handleUSERS(Message message) {
		for (Peer user : message.getUsersList()) {
			if (user.getIp().compareTo(_me.getIp()) != 0) {
				addAddress(user);
			}
		}
		//TODO Send to gui
	}
	
	private void handleJOIN(Message message) {
		if (!channelExists(message.getChannelID())) {
			// TODO send invalid channel
			System.out.println("channel doesnt exist");
			return;
		}
		JoinResponse jr = _channelList.get(message.getChannelID()).addUserByMessage(message);
		sendJoinResponseMessage(jr, message);
		System.out.println("handlejoin");
	}
	
	private void handlePASS(Message message) {
		if (!channelExists(message.getChannelID())) {
			// TODO send invalid channel
			return;
		}
		JoinResponse jr = _channelList.get(message.getChannelID()).addUserByMessage(message);
		sendJoinResponseMessage(jr, message);
	}
	
	private void handleJOINED(Message message) {
		_graphicInterface.addWindow(message.getOrigin().getIp(), message.getChannelID());
	}
	
	private void addAddress(Peer address) {
		_addressBook.addAddress(address);
		_graphicInterface.updateUserList();
	}
	
	public void createChannel(String channel) {
		Channel ch = new Channel(channel, _me);
		_channelList.put(channel, ch);
	}
	
	public void sendLISTCH(String IP) { _messageBuilder.sendLISTCH(IP); }
	
	public void sendJOIN(String IP, String channel) { _messageBuilder.sendJOIN(IP, channel);}
	
	public void sendJOINED(String IP, String channel) { _messageBuilder.sendJOINED(IP, channel);}
	
	public void sendINVALIDPASS(String IP, String channel) {_messageBuilder.sendINVALIDPASS(IP, channel);}
	
//	public void sendINVALIDCH()
	public void sendJoinResponseMessage(JoinResponse jr, Message msg) {_messageBuilder.sendJoinResponseMessage(jr, msg);}
	
	
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
	
	private MessageBuilder _messageBuilder;
}
