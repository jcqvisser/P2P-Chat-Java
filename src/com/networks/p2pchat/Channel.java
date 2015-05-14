package com.networks.p2pchat;

import java.util.ArrayList;

import com.networks.p2pchat.Message.MessageType;

public class Channel {
	
	// TODO Jacques needs to finish channel

	public Channel(String channelName, Peer me, String channelPass) {
		_channelName = channelName;
		_me = me;
		_users = new ArrayList<Peer>();
	}
	
	public boolean addUser(Message newUserMessage) {
		if (newUserMessage.getMessageType() != MessageType.JOIN ||
				newUserMessage.getMessageType() != MessageType.PASS) {
			return false;
		}
		
		if (newUserMessage.getMessageType() == MessageType.JOIN || 
				_channelPass.compareTo("") == 0) {
			_users.add(newUserMessage.getOrigin());
			return true;
		}
		
		if (newUserMessage.getMessageType() == MessageType.PASS ||
				_channelPass.compareTo(newUserMessage.getText()) == 0) {
			_users.add(newUserMessage.getOrigin());
			return true;
		}
		
		return false;
	}
	
	private ArrayList<Peer> _users;
	private Peer _me;
	private String _channelName;
	private String _channelPass;

}
