package com.networks.p2pchat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
	// Takes in a post office object for passing messages.
	public ConversationHolder(PostOffice postOffice) {
		_postOffice = postOffice;
		_conversations = new ArrayList<Conversation>();
	}
	
	// Send a message on to a peer in the list of conversations
	// Or start a new conversation if necessary.
	public synchronized boolean sendMessage(Message message) {
		int index = findConversationID(message.getDestination().getIp());
		if(index == -1) {
			// Logic if the conversation does not exist.
		} else {
			// Send the message on to the target converstation.
			_conversations.get(index).Send(message);
		}
		return false;
	}
	
	// Handle the received message.
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
	// Find the ID of the conversation in the list, defined by IP.
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
	private PostOffice _postOffice;
	private ArrayList<Conversation> _conversations;
}
