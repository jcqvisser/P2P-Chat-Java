package com.networks.p2pchat;

import java.util.HashMap;

import com.networks.p2pchat.Message.MessageType;

public class Channel {
	/**
	 * Constructor for a channel without a password.
	 * @param channelName : a string to be used for the channel name
	 * @param me : a peer object that represents the creator of the channel
	 */
	public Channel(String channelName, Peer me) {
		_channelName = channelName;
		_me = me;
		_channelPass = "";
		_users = new HashMap<String,String>();
		addUser(_me);
	}
	/**
	 * Constructor for a channel with a password.
	 * @param channelName : A string to be used for the channel name.
	 * @param me : a peer object representing the creator of the channel.
	 * @param channelPass : a string to be used as the password for joining the channel.
	 */
	public Channel(String channelName, Peer me, String channelPass) {
		_channelName = channelName;
		_me = me;
		_users = new HashMap<String, String>();
		_channelPass = channelPass;
		addUser(_me);
	}
	
	
	/**
	 * An enum to be used as a response to a join request.
	 * @author jcqvi_000
	 *
	 */
	public enum JoinResponse { 
		/**
		 * Join request was valid: the individual has joined the channel.
		 */
		DETAILS_CORRECT, 
		
		/**
		 * A {@link MessageType#PASS} {@link Message} must be sent as a joinMessage, the channel has a password.
		 */
		 SUPPLY_PASSWORD, 
		
		 /**
		  * The password supplied in the PASS {@link Message} message is invalid, it does not match that of the channel.
		  */
		 INVALID_PASSWORD, 
		 
		 /**
		  * Used as a response when a user attempts to join a channel with a message other than JOIN or PASS.
		  */
		 LOGIN_REQUIRED,
		 
		 /**
		  * The channel name supplied does not match that of the channel.
		  */
		 WRONG_CHANNEL,
		 
		 /**
		  * The user trying to join the channel is already a member thereof.
		  */
		 ALREADY_JOINED,
		 
		 /**
		  * The message has been sent to the wrong user.
		  */
		 WRONG_USER
		 
	}
	
/**
 * Adds a user by passing hte appropriate message from him/her to it.
 * 
 * If the channel has a password, then a PASS message needs to be supplied, 
 * if not, a JOIN message will do
 * @param joinMessage a message object that will be used for validation of channel joining, ideally a JOIN or PASS message
 * @return returns a {@link JoinResponse} enum saying whether the user is authorized to join the channel
 */
	public JoinResponse addUserByMessage(Message joinMessage) { 
		if (joinMessage.getDestination().getIp().compareTo(_me.getIp()) != 0) {
			return JoinResponse.WRONG_USER;
		}
		
		if (joinMessage.getChannelID().compareTo(_channelName) != 0) {
			return JoinResponse.WRONG_CHANNEL;
		}
		
		if (joinMessage.getMessageType() != MessageType.JOIN &&
				joinMessage.getMessageType() != MessageType.PASS) {
			return JoinResponse.LOGIN_REQUIRED;
		}
		
		if (joinMessage.getMessageType() == MessageType.JOIN && 
				(_channelPass == null || _channelPass.compareTo("") == 0 )){
			return addUser(joinMessage.getOrigin());
		}
		
		if (joinMessage.getMessageType() == MessageType.PASS &&
				_channelPass.compareTo(joinMessage.getText()) == 0) {
			return addUser(joinMessage.getOrigin());
		}
		
		if (joinMessage.getMessageType() == MessageType.JOIN && (_channelPass.compareTo("") != 0 || _channelPass != null)) {
			return JoinResponse.INVALID_PASSWORD;
		}	
		
		return JoinResponse.LOGIN_REQUIRED;
	}
	
	/**
	 * Add a user without much verification
	 * @param user the user to be 
	 * @return {@link JoinResponse#ALREADY_JOINED} or {@link JoinResponse#DETAILS_CORRECT }
	 */
	private JoinResponse addUser(Peer user) {
		if (hasUser(user)) {
			return JoinResponse.ALREADY_JOINED;
		}
		_users.put(user.getIp(), user.getId());
		return JoinResponse.DETAILS_CORRECT ;
	}
	
	/**
	 * Evaluates whether a user is part of a channel.
	 * @param user : A Peer object representing the user to check membership of the group of.
	 * @return returns true if the user is a member of the channel, false otherwise.
	 */
	public boolean hasUser(Peer user) {
		if (_users.containsKey(user.getIp())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Removes a user from the channel, that user will have to rejoin (and supply the password if neccessary) if they want to continue posting messages to this channel.
	 * @param user
	 * @return returns true is user has been succesfully removes, false otherwise. Could return false if user was never part of the group.
	 */
	public boolean removeUser(Peer user) {
		if (!hasUser(user)) {
			return false;
		}
		_users.remove(user.getIp());
		return true;
	}
	
	/**
	 * Get a hashmap of all the users.
	 * @return Hashmap<String, String>
	 */
	public HashMap<String, String> getUsers() {
		return _users;
	}
	
	/**
	 * A Map of users. Not peer objects, but rather IP's mapped to ID's, both strings.
	 */
	private HashMap<String, String> _users;
	
	/**
	 * A Peer object representing the owner of the channel
	 */
	private Peer _me;
	
	/**
	 * A String value representing the name of the channel, or its ID
	 */
	private String _channelName;
	
	/**
	 * A string value representing the password for the channel
	 */
	private String _channelPass;

}
