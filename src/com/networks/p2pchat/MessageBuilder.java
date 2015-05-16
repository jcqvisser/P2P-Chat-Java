/**
 * 
 */
package com.networks.p2pchat;

import com.networks.p2pchat.Channel.JoinResponse;
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
	
	public void sendJOINED(String IP, String channelId){
		Message msg = new Message(MessageType.JOINED,
				_me,
				_addressBook.getAddress(IP),
				channelId);
		_conversationHolder.sendMessage(msg);
	}
	
	public void sendINVALIDPASS(String IP, String channel) {
		Message msg = new Message(MessageType.INVALIDPASS,
					_me,
					_addressBook.getAddress(IP),
					channel);
		_conversationHolder.sendMessage(msg);
	}
	
	public void sendINVALIDCH(String IP, String channel) {
		Message msg = new Message(MessageType.INVALIDCH,
				_me,
				_addressBook.getAddress(IP),
				channel);
		_conversationHolder.sendMessage(msg);
	}
	
	public void sendJoinResponseMessage(JoinResponse jr, Message message) {
		System.out.println("sendjoinrespoenfafewa" + jr);
		switch (jr) {
		case ALREADY_JOINED:
			break;
		case DETAILS_CORRECT:
			System.out.println("details correct");
			sendJOINED(message.getOrigin().getIp(), message.getChannelID());
			break;
		case INVALID_PASSWORD:
			sendINVALIDPASS(message.getOrigin().getIp(), message.getChannelID());
			break;
		case LOGIN_REQUIRED:
			sendINVALIDPASS(message.getOrigin().getIp(), message.getChannelID());
			break;
		case SUPPLY_PASSWORD:
			sendINVALIDPASS(message.getOrigin().getIp(), message.getChannelID());
			break;
		case WRONG_CHANNEL:
			sendINVALIDCH(message.getOrigin().getIp() , message.getChannelID());
			break;
		case WRONG_USER:
			break;
		}
	}
	
	private ConversationHolder _conversationHolder;
	private Peer _me;
	private AddressBook _addressBook;
	private int _ttl;

}
