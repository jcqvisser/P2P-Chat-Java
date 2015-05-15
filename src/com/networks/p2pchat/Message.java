package com.networks.p2pchat;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * "Already implemented has an efficiency all it's own" - Jacques Visser 2015
 * @author jcqvi_000
 *
 */
@XmlRootElement( namespace = "com.networks.p2pchat")
public class Message {
	
	public enum FileType {
		JPG, PNG, GIF, TIFF, BMP
	}	
	
	public enum MessageType {
		HELO, HI, LISTCH, CH, JOIN, NICK, PASS, LISTUSERS, USERS, MSGCH, FILE,
		QUIT, MSG, REPEAT, INVALIDPASS, NOTAMEMBER
	}

	public Message(){}
	
	// HELO, HI, LISTCH, NICK, LISTUSERS
	public Message(	MessageType msgType, 
			int ttl, 
			Peer origin,
			Peer source,
			Peer destination) {
		this.setMessageType(msgType);
		this.setTtl(ttl);
		this.setOrigin(origin);
		this.setSource(source);
		this.setDestination(destination);
	}	
	
	// CH
	public Message(MessageType msgType,
				Peer origin,
				Peer destination,
				ArrayList<String> channels){
		this.setMessageType(msgType);
		this.setOrigin(origin);
		this.setDestination(destination);
		this.setChannels(channels);
	}
	
	//USERS
	public Message(MessageType msgType,
			Peer origin,
			ArrayList<Peer> peers,
			Peer destination){
		this.setMessageType(msgType);
		this.setOrigin(origin);
		this.setDestination(destination);
		this.setUsers(peers);
	}
	
	// MSG, PASS
	public Message(	MessageType msgType, 
			Peer origin,
			Peer destination,
			String channelID,
			String text) {
		this.setMessageType(msgType);
		this.setOrigin(origin);
		this.setDestination(destination);
		this.setChannelID(channelID);
		this.setText(text);
	}
	
	// MSGCH 
	public Message(	MessageType msgType, 
			Peer source,
			Peer origin,
			Peer destination,
			String channelID,
			String text) {
		this.setMessageType(msgType);
		this.setSource(source);
		this.setOrigin(origin);
		this.setDestination(destination);
		this.setChannelID(channelID);
	}
	
	// JOIN, QUIT, REPEAT, INVALIDPASS, NOTAMEMBER
	public Message(	MessageType msgType, 
			Peer origin,
			Peer destination,
			String channelID) {
		this.setMessageType(msgType);
		this.setOrigin(origin);
		this.setDestination(destination);
		this.setChannelID(channelID);
	}
		
	
	// FILE
	public Message(	MessageType msgType, 
			Peer origin,
			Peer source,
			Peer destination,
			String channelID,
			byte[] file,
			String filename,
			FileType fType,
			int blockNo,
			int totalBlocks) {
		this.setMessageType(msgType);
		this.setOrigin(origin);
		this.setSource(source);
		this.setDestination(destination);
		this.setChannelID(channelID);
		this.setData(file);
		this.setFilename(filename);
		this.setFileType(fType);
		this.setBlockNumber(blockNo);
		this.setTotalBlocks(totalBlocks);
	}	
	
	public Message(Message msg)
	{
		this.setMessageType(msg.getMessageType());
		this.setChannelID(msg.getChannelID());
		this.setChannels(msg.getChannelList());
		this.setData(msg.getData());
		this.setDestination(msg.getDestination());
		this.setFilename(msg.getFilename());
		this.setForwardable(msg.getForwardable());
		this.setOrigin(msg.getOrigin());
		this.setSource(msg.getSource());
		this.setText(msg.getText());
		this.setTtl(msg.getTtl());
		this.setUsers(msg.getUsersList());
		this.setFileType(msg.getFileType());
	}
	
	public Message forward(Peer destination) throws IOException {
		if (!this.getForwardable()) throw new IOException();
		
		Message fwd =  new Message(this);
		fwd.setSource(fwd.getDestination());
		fwd.setDestination(destination);
		return fwd;
	}
	
	// Getters
	public MessageType getMessageType() {return _mType;}
	public int getTtl() {return _ttl;}
	public Peer getOrigin() {return _origin;}
	public Peer getSource() {return _source;}
	public Peer getDestination() {return _destination;}
	public String getChannelID() {return _channelID;}
	public String getText() {return _text;}
	public String getFilename() {return _filename;}
	public byte[] getData() {return _data;}
	public ArrayList<String> getChannelList() {return _channels;}
	public boolean getForwardable() {return _forwardable;}
	public FileType getFileType() {return _fType;}
	public ArrayList<Peer> getUsersList() {return _users;}
	public int getBlockNumber() {return _blockNumber;}
	public int getTotalBlocks() {return _totalBlocks;}
	
	// Setters
	public void setMessageType(MessageType a) {_mType = a;}
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
	public void setFileType(FileType a) {_fType = a;}
	public void setUsers(ArrayList<Peer> a) {_users = a;}
	public void setBlockNumber(int a) {_blockNumber = a;}
	public void setTotalBlocks(int a) {_totalBlocks = a;}
	

	private MessageType _mType;
	private int _ttl;
	private Peer _origin;
	private Peer _source;
	private Peer _destination;
	private String _channelID;
	private String _text;
	private String _filename;
	private byte[] _data;
	private int _blockNumber;
	private int _totalBlocks;
	@XmlElementWrapper(name = "channels")
	@XmlElement(name = "channel")
	private ArrayList<String> _channels;
	private boolean _forwardable;
	private FileType _fType;
	@XmlElementWrapper(name = "users")
	@XmlElement(name = "peer")
	private ArrayList<Peer> _users;
	
	
	
}
