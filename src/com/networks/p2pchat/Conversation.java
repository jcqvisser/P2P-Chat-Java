/**
 * 
 */
package com.networks.p2pchat;

import java.io.IOException;
import java.net.Socket;

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
	 * @throws IOException 
	 */
	public Conversation(ConversationHolder convHolder, Socket socket) throws IOException {
		_convHolder = convHolder;
		_socket = socket;
		_receiver = new Receiver(_socket.getInputStream(), this);
		_sender = new Sender(_socket.getOutputStream());
	
		(new Thread(_receiver)).start();
	}
	
	@Override
	public void run() {
		while (_runThread){
			
		}
	}
	
	/*get the IP address of the person with which the conversation is*/
	public String getRecipientIp() {
		return _socket.getInetAddress().toString();
	}
	
	/* Handlemessage passes the message upwards to the conversationholder class that holds this conversation*/
	public void handleMessage(Message msg) {
		_convHolder.handleMessage(msg);
	}
	
	/* Send makes use of the sender object to send a message passed down by the conversationholder class*/
	public void Send(Message msg) {
		_sender.sendMessage(msg);
	}
	
	/* holder class for conversations, conv's need to interact with it.*/
	private ConversationHolder _convHolder;
	
	/*Socket object, used to create sender and receiver, might need to be closed at some point*/
	private Socket _socket;
	
	/*Sender object, not threaded, holds outputstream object pointed at a socket, called to send messsages */
	private Sender _sender;
	
	/*Receiver object, threaded, holds inputstream object pointed at _socket, receives messages and notifies the conversation object holding it*/
	private Receiver _receiver;
	
	/* boolean value to see whether this thread must be closed*/
	private volatile boolean _runThread; 
	
	//TODO close sockets when done
	
}
