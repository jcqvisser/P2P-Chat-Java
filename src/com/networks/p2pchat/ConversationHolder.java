package com.networks.p2pchat;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Conversation holding class, handles logic of which conversation
 * a particular message should be sent to.
 * 
 * @author Anthony
 * @author Jacques
 *
 */

public class ConversationHolder {
	/**
	 * Takes in a post office object for passing messages,
	 * and the port that listen servers are running on. 
	 * @param postOffice
	 * @param port
	 */
	public ConversationHolder(PostOffice postOffice, int port) {
		_postOffice = postOffice;
		_conversations = new HashMap<String, Conversation>();
		_addressBook = AddressBook.getInstance();
		_port = port;
	}
	
	/**
	 * Send a message on to a peer in the list of conversations
	 * Or start a new conversation if necessary.
	 * @param message
	 * @return
	 */
	public synchronized boolean sendMessage(Message message) {
		if(_conversations.containsKey(message.getDestination().getIp())) {
			// Send the message on to the target conversation.
			_conversations.get(message.getDestination().getIp()).Send(message);
			return true;
		} else {
			// Logic if the conversation does not exist.
			if(_addressBook.addressExists(message.getDestination().getIp())) {
				try {
					addConversation(new Socket(message.getDestination().getIp(), _port));
					_conversations.get(message.getDestination().getIp()).Send(message);
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
	
	/**
	 * Close the conversation on a particular socket 
	 * @param ipAddr
	 */
	public synchronized void closeConversation(String ipAddr) {
		if(_conversations.containsKey(ipAddr) && _conversations.get(ipAddr) != null) {
			_conversations.get(ipAddr).close();
			_conversations.remove(ipAddr);
		}
	}
	
	/**
	 * Handle the received message from a particular conversation, 
	 * will pass the message to the post office object.
	 * @param message
	 */
	public synchronized void handleMessage(Message message) {
		_postOffice.handleMessage(message);
	}
	
	/**
	 * The add conversation function will take in a socket and create 
	 * a conversation for that particular socket, normally only called
	 * on initialization from outside class.
	 * @param connectionSocket
	 * @return
	 */
	public synchronized boolean addConversation(Socket connectionSocket) {
		if(!_conversations.containsKey(connectionSocket.getInetAddress().getHostAddress())) {
			try{ 
				_conversations.put(connectionSocket.getInetAddress().getHostAddress(), new Conversation(this, connectionSocket));
			} catch(IOException ioe) {
				System.err.println("Error creating new conversation: " + ioe);
			}
			return true;
		}
		return false;
	}
	
	// Private member variables:
	/**
	 * Store the holding class, PostOffice.
	 */
	private PostOffice _postOffice;
	/**
	 * List of active conversations.
	 */
	private Map<String, Conversation> _conversations;
	/**
	 * Address book singleton, allows for a new conversation to opened if the address is known.
	 */
	private AddressBook _addressBook;
	/**
	 * Port that the server connections will be listening on.
	 */
	private int _port;
}
