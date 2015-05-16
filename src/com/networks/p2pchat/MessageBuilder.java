/**
 * 
 */
package com.networks.p2pchat;

import com.networks.p2pchat.Message.MessageType;

/**
 * @author jcqvi_000
 *
 */
public class MessageBuilder {

	/**
	 * Standard constructor for the MessageBuilder Wrapper class, takes a ConversationHolder
	 * object (which comes from PostOffice) which is used to send the messages.
	 * @param convHolder
	 */
	public MessageBuilder(ConversationHolder convHolder, Peer me) {
		_conversationHolder = convHolder;
		_me = me;
		_addressBook = AddressBook.getInstance();
	}
	
	public void updateMePeer(Peer me) {
		_me = me;
	}
	
	public void sendLISTCH(String IP) {
		Message msg = new Message(MessageType.LISTCH,
								0,
								_me,
								_me,
								_addressBook.getAddress(IP));
		_conversationHolder.sendMessage(msg);
	}
	
	public void sendJOIN(String IP, String channel) {
		if (channel.compareTo("private") == 0) {
			return;
		}
		Message msg = new Message(MessageType.JOIN,
				_me,
				_addressBook.getAddress(IP),
				channel);
		_conversationHolder.sendMessage(msg);
		
	}
	
	public void sendHELO(String IP) {
		Message msg = new Message(MessageType.HELO,
							_ttl,
							_me,
							_me,
							new Peer("", IP));
		_conversationHolder.sendMessage(msg);
	}
	
	private ConversationHolder _conversationHolder;
	private Peer _me;
	private AddressBook _addressBook;
	private int _ttl;

}
