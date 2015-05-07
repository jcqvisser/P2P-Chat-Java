package com.networks.p2pchat;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "message")
@XmlType(propOrder = {"mType", "ttl", "origin", "source", "destination", 
					  "channelID", "text", "filename", "data", "channels", 
					  "forwardable", "peers"})
public class Message {
	
	public enum FileType {
		JPG, PNG
	}	
	
	public enum messageType {
		HELO, HI, LISTCH, CH, JOIN, NICK, PASS, LISTUSERS, USERS, MSGCH, FILE,
		QUIT, MSG, REPEAT
	}

	// HELO, HI, LISTCH, NICK, LISTUSERS
	public Message(	messageType msgType, 
			int ttl, 
			Peer origin,
			Peer source,
			Peer destination) {
	}	
	
	// CH
	public Message(messageType msgType,
				Peer origin,
				Peer destination,
				String[] channels){
	}
	
	//USERS
	public Message(messageType msgType,
			Peer origin,
			Peer destination,
			Peer[] channels){
}
	
	// MSG, PASS
	public Message(	messageType msgType, 
			Peer origin,
			Peer destination,
			int channelID,
			String text) {
	}
	
	// MSGCH 
	public Message(	messageType msgType, 
			Peer source,
			Peer origin,
			Peer destination,
			int channelID,
			String text) {
	}
	
	// JOIN, QUIT, REPEAT
	public Message(	messageType msgType, 
			Peer origin,
			Peer destination,
			int channelID) {
	}
		
	
	// FILE
	public Message(	messageType msgType, 
			Peer origin,
			Peer source,
			Peer destination,
			int channelID,
			byte[] file,
			FileType fType) {
	}	
	
	public Message(Message msg, boolean forward) {
		// forward message constructor
		// must make sure that original source is intact
	}
	
	// Getters
	public messageType getMessageType() {return _mType;}
	public int getTTL() {return _ttl;}
	public Peer getOrigin() {return _origin;}
	public Peer getSource() {return _source;}
	public Peer getDestination() {return _destination;}
	public String getChannelID() {return _channelID;}
	public String getText() {return _text;}
	public String getFilename() {return _filename;}
	public byte[] getData() {return _data;}
	public String[] getChannels() {return _channels;}
	public boolean getForwardable() {return _forwardable;}
	public Peer[] getPeers() {return _peers;}
	
	// Setters
	public void setMessageType(messageType a) {_mType = a;}
	public void setTTL(int a) {_ttl = a;}
	public void setOrigin(Peer a) {_origin = a;}
	public void setSource(Peer a) {_source = a;}
	public void setDestination(Peer a) {_destination = a;}
	public void setChannelID(String a) {_channelID = a;}
	public void setText(String a) {_text = a;}
	public void setFilename(String a) {_filename = a;}
	public void setData(byte[] a) {_data = a;}
	public void setChannels(String[] a) {_channels = a;}
	public void setForwardable(boolean a) {_forwardable = a;}
	public void setPeers(Peer[] a) {_peers = a;}
	
	private messageType _mType;
	private int _ttl;
	private Peer _origin;
	private Peer _source;
	private Peer _destination;
	private String _channelID;
	private String _text;
	private String _filename;
	private byte[] _data;
	private String[] _channels;
	private boolean _forwardable;
	private Peer[] _peers;
	
	
}
