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
	
	public messageType getMessageType() {return _mType}
	public int getTTL() {return _ttl}
	public Peer getOrigin() {return _origin}
	public Peer getSource() {return _source}
	public Peer getDestination() {return _destination}
	public String getChannelID() {return _channelID}
	public String getText() {return _text}
}
