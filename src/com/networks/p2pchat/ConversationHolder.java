package com.networks.p2pchat;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Conversation holding class, handles logic of which conversation
 * a particular message should be sent to.
 * 
 * @author Anthony
 *
 */

public class ConversationHolder {
	/*
	 *  Takes in a post office object for passing messages,
	 *  and the port that listen servers are running on.  
	 */
	public ConversationHolder(PostOffice postOffice, int port) {
		_postOffice = postOffice;
		_conversations = new ArrayList<Conversation>();
		_addressBook = AddressBook.getInstance();
		_port = port;
	}
	
	/**
	 * Send a message on to a peer in the list of conversations
	 * Or start a new conversation if necessary.
	 */
	public synchronized boolean sendMessage(Message message) {
		int index = findConversationID(message.getDestination().getIp());
		if(index != -1) {
			// Send the message on to the target conversation.
			_conversations.get(index).Send(message);
			return true;
		} else {
			// Logic if the conversation does not exist.
			if(_addressBook.addressExists(message.getDestination().getIp())) {
				try {
					System.out.println(message.getDestination().getIp());
					System.out.println(findConversationID(message.getDestination().getIp()));
					addConversation(new Socket(message.getDestination().getIp(), _port));
					_conversations.get(findConversationID(message.getDestination().getIp())).Send(message);
					return true;
				} catch ( IOException e ) {
					System.err.println("Error creating connection: " + e);
					// Remove connections from the address book that are unreachable.
					_addressBook.removeAddress(message.getDestination().getIp());
					return false;
				}
			}
		}
		return false;
	}
	
	/* 
	 * Close the conversation on a particular socket 
	 */
	public synchronized void closeConversation(String ipAddr) {
		int index = findConversationID(ipAddr);
		if(index != -1 && _conversations.get(index) != null) {
			_conversations.get(index).close();
			_conversations.remove(index);
		}
	}
	
	/*
	 *  Handle the received message.
	 */
	public synchronized void handleMessage(Message message) {
		_postOffice.handleMessage(message);
	}
	
	public synchronized boolean addConversation(Socket connectionSocket) {
		if(findConversationID(connectionSocket.getInetAddress().getHostAddress()) == -1) {
			try{ 
				_conversations.add(new Conversation(this, connectionSocket));
			} catch(IOException ioe) {
				System.err.println("Error creating new conversation: " + ioe);
			}
			
			return true;
		}
		return false;
	}
	
	// Private member functions:
	/*
	 *  Find the ID of the conversation in the list, defined by IP.
	 */
	private int findConversationID(String ip) {
		ListIterator<Conversation> itr = _conversations.listIterator();
		int index = 0;
		while(itr.hasNext()) {
			index = itr.nextIndex();
			if(itr.next().getRecipientIp().compareTo(ip) == 0) {
				return index;
			}
		}
		return -1;
	}
	
	// Private member variables:
	/*
	 * Store the holding class, PostOffice.
	 */
	private PostOffice _postOffice;
	/*
	 * List of active conversations.
	 */
	private ArrayList<Conversation> _conversations;
	/*
	 * Address book singleton, allows for a new conversation to opened if the address is known.
	 */
	private AddressBook _addressBook;
	/*
	 * Port that the server connections will be listening on.
	 */
	private int _port;
}
