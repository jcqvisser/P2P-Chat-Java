package com.networks.p2pchat;

import java.net.Socket;
import java.util.ArrayList;

public class ConversationHolder {
	// Takes in a post office object for passing messages.
	public ConversationHolder(PostOffice postOffice) {
		_postOffice = postOffice;
	}
	
	// Send a message on to a peer in the list of conversations
	// Or start a new conversation if necessary.
	public synchronized boolean sendMessage(Message message) {
		return false;
	}
	
	// Handle the received message.
	public synchronized boolean handleMessage(Message message) {
		return false;
	}
	
	public synchronized boolean addConversation(Socket connectionSocket) {
		return false;
	}
	
	// Private member functions:
	// Find the ID of the conversation in the list, defined by IP.
	private int findConversationID(String ip) {
		ListIterator<Conversation> itr = _conversations.listIterator();
		int index = 0;
		while(itr.hasNext()) {
			index = itr.nextIndex();
			if(itr.next().getIP().compareTo(ip) == 0) {
				return index;
			}
		}
		return -1;
	}
	
	// Private member variables:
	private PostOffice _postOffice;
	private ArrayList<Conversation> _conversations;
}
