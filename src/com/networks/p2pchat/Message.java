package com.networks.p2pchat;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( namespace = "com.networks.p2pchat")
public class Message {
	
	public enum FileType {
		JPG, PNG
	}	
	
	public enum messageType {
		HELO, HI, LISTCH, CH, JOIN, NICK, PASS, LISTUSERS, USERS, MSGCH, FILE,
		QUIT, MSG, REPEAT
	}

	public Message(){}
	
	// HELO, HI, LISTCH, NICK, LISTUSERS
	public Message(	messageType msgType, 
			int ttl, 
			Peer origin,
			Peer source,
			Peer destination) {
		// TODO Implement
	}	
	
	// CH
	public Message(messageType msgType,
				Peer origin,
				Peer destination,
				String[] channels){
		// TODO Implement
	}
	
	//USERS
	public Message(messageType msgType,
			Peer origin,
			Peer destination,
			Peer[] channels){
		// TODO Implement
	}
	
	// MSG, PASS
	public Message(	messageType msgType, 
			Peer origin,
			Peer destination,
			int channelID,
			String text) {
		// TODO Implement
	}
	
	// MSGCH 
	public Message(	messageType msgType, 
			Peer source,
			Peer origin,
			Peer destination,
			int channelID,
			String text) {
		// TODO Implement
	}
	
	// JOIN, QUIT, REPEAT
	public Message(	messageType msgType, 
			Peer origin,
			Peer destination,
			int channelID) {
		// TODO Implement
	}
		
	
	// FILE
	public Message(	messageType msgType, 
			Peer origin,
			Peer source,
			Peer destination,
			int channelID,
			byte[] file,
			FileType fType) {
		// TODO Implement
	}	
	
	public Message(Message msg, boolean forward) {
		// forward message constructor
		// TODO must make sure that the msg is forwardable
		// TODO Implement
	}
	
	// Getters
	public messageType getMessageType() {return _mType;}
	public int getTtl() {return _ttl;}
	public Peer getOrigin() {return _origin;}
	public Peer getSource() {return _source;}
	public Peer getDestination() {return _destination;}
	public String getChannelID() {return _channelID;}
	public String getText() {return _text;}
	public String getFilename() {return _filename;}
	public byte[] getData() {return _data;}
	public ArrayList<String> getChannels() {return _channels;}
	public boolean getForwardable() {return _forwardable;}
	public ArrayList<Peer> getUsersList() {return _users;}
	
	// Setters
	public void setMessageType(messageType a) {_mType = a;}
	public void setTtl(int a) {_ttl = a;}
	public void setOrigin(Peer a) {_origin = a;}
	public void setSource(Peer a) {_source = a;}
	public void setDestination(Peer a) {_destination = a;}
	public void setChannelID(String a) {_channelID = a;}
	public void setText(String a) {_text = a;}
	public void setFilename(String a) {_filename = a;}
	public void setData(byte[] a) {_data = a;}
	public void setChannels(ArrayList<String> a) {_channels = a;}
	public void setForwardable(boolean a) {_forwardable = a;}
	public void setUsers(ArrayList<Peer> a) {_users = a;}
	

	private messageType _mType;
	private int _ttl;
	private Peer _origin;
	private Peer _source;
	private Peer _destination;
	private String _channelID;
	private String _text;
	private String _filename;
	private byte[] _data;
	private ArrayList<String> _channels;
	private boolean _forwardable;
	@XmlElementWrapper(name = "users")
	@XmlElement(name = "peer")
	private ArrayList<Peer> _users;
	
	
}
