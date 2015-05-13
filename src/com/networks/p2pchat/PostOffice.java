package com.networks.p2pchat;

import java.net.Socket;

public class PostOffice {
	// Create the postoffice object.
	public PostOffice() {
		_conversationHolder = new ConversationHolder(this);
	}
	
	// Add a new conversation object for a particular socket.
	public synchronized boolean addConversation(Socket connectionSocket) {
		return _conversationHolder.addConversation(connectionSocket);
	}
	
	// Handle the received message.
	public synchronized void handleMessage(Message message) {
		// Message send logic.
	}
	
	// Private member variables:
	private ConversationHolder _conversationHolder;
}
