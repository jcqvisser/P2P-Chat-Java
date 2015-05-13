/**
 * 
 */
package com.networks.p2pchat;

/**
 * Embodies a conversation
 * Holds sender and receiver objects
 * takes a conversationholder object in the constructor (this also holds the conversation object)
 * senders are not threaded, receivers are
 * @author jcqvi_000
 *
 */
public class Conversation implements Runnable{

	/**
	 * Standard constructor.
	 * Takes a conversationholder object which holds this conversation
	 * Created from within a conversationholder
	 */
	public Conversation(ConversationHolder convHolder) {
		
	}
	
	/* holder class for conversations, conv's need to interact with it.
	 * 
	 */
	ConversationHolder _convHolder;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
