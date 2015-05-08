package com.networks.p2pchat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
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
		this.setMessageType(msgType);
		this.setTtl(ttl);
		this.setOrigin(origin);
		this.setSource(source);
		this.setDestination(destination);
	}	
	
	// CH
	public Message(messageType msgType,
				Peer origin,
				Peer destination,
				ArrayList<String> channels){
		this.setMessageType(msgType);
		this.setOrigin(origin);
		this.setDestination(destination);
		this.setChannels(channels);
	}
	
	//USERS
	public Message(messageType msgType,
			Peer origin,
			ArrayList<Peer> peers,
			Peer destination){
		this.setMessageType(msgType);
		this.setOrigin(origin);
		this.setDestination(destination);
		this.setUsers(peers);
	}
	
	// MSG, PASS
	public Message(	messageType msgType, 
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
	public Message(	messageType msgType, 
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
	
	// JOIN, QUIT, REPEAT
	public Message(	messageType msgType, 
			Peer origin,
			Peer destination,
			String channelID) {
		this.setMessageType(msgType);
		this.setOrigin(origin);
		this.setDestination(destination);
		this.setChannelID(channelID);
	}
		
	
	// FILE
	public Message(	messageType msgType, 
			Peer origin,
			Peer source,
			Peer destination,
			String channelID,
			byte[] file,
			String filename) {
		this.setMessageType(msgType);
		this.setOrigin(origin);
		this.setSource(source);
		this.setDestination(destination);
		this.setChannelID(channelID);
		this.setData(file);
		this.setFilename(filename);
		// TODO add file type enum thing
	}	
	
	public Message(Message msg, boolean forward) {
		// forward message constructor
		// TODO must make sure that the msg is forwardable
		// TODO Implement
	}
	
	public Message(Message msg)
	{
		this.setMessageType(msg.getMessageType());
		this.setChannelID(msg.getChannelID());
		this.setChannels(msg.getChannels());
		this.setData(msg.getData());
		this.setDestination(msg.getDestination());
		this.setFilename(msg.getFilename());
		this.setForwardable(msg.getForwardable());
		this.setOrigin(msg.getOrigin());
		this.setSource(msg.getSource());
		this.setText(msg.getText());
		this.setTtl(msg.getTtl());
		this.setUsers(msg.getUsersList());
	}
	
	public Message(InputStream stream) throws JAXBException{
        this(readMessage(stream));
	}
	
	private static Message readMessage(InputStream stream) throws JAXBException {
		JAXBContext context;
        context = JAXBContext.newInstance(Message.class);
        Unmarshaller um = context.createUnmarshaller();
        return (Message) um.unmarshal(stream);
	}
	
	public void send(OutputStream outStream) throws IOException, JAXBException {
	    JAXBContext context = JAXBContext.newInstance(Message.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	    m.marshal(this, outStream);
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
