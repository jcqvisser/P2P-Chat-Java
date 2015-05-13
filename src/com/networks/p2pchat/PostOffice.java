package com.networks.p2pchat;

import java.net.Socket;

public class PostOffice {
	public PostOffice() {
		_conversationHolder = new ConversationHolder(this);
	}
	
	public synchronized boolean addConversation(Socket connectionSocket) {
		return true;
	}
	
	public synchronized void handleMessage(Message message) {
		
	}
	
	// Private member variables:
	private ConversationHolder _conversationHolder;
}
