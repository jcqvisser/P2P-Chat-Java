/**
 * 
 */
package com.networks.p2pchat;

/**
 * Embodies a conversation
 * Holds sender and receiver objects
 * Senders are not threaded, receivers are
 * 
 * Takes a conversationholder object in the constructor (this also holds the conversation object)
 * 
 * Takes a Socket object, from which input and output streams are created.
 * The input and outmut streams are used in constructing the sender and receiver objects.
 * @author jcqvi_000
 *
 */
public class Conversation implements Runnable{

	/**
	 * Standard constructor.
	 * Takes a conversationholder object which holds this conversation
	 * Created from within a conversationholder
	 */
	public Conversation(ConversationHolder convHolder, Socket socket) {
		_convHolder = convHolder;
		_socket = socket;
		
		// TODO more things need to be  created 
	}
	
	@Override
	public void run() {
		// TODO implement
		
	}
	
	public String getRecipientIp() {
	// TODO implement	
	}
	
	/* holder class for conversations, conv's need to interact with it.*/
	ConversationHolder _convHolder;
	
	/*Socket object, used to create sender and receiver, might need to be closed at some point*/
	Socket _socket;
	
	/*Sender object, not threaded, holds outputstream object pointed at a socket, called to send messsages */
	Sender _sender;
	
	/*Receiver object, threaded, holds inputstream object pointed at _socket, receives messages*/
	
}
