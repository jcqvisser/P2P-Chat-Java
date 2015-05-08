package com.networks.p2pchat;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "peer")
public class Peer {
	// Public members.
	public Peer(String id, String ipAddr, int port) {
		_id = id;
		_ipAddr = ipAddr;
		_port = port;
	}
	
	public Peer() {}
	
	public String getId() {
		return _id;
	}
	
	public String getIp() {
		return _ipAddr;
	}
	
	public int getPort() {
		return _port;
	}
	
	public void setId(String id) {
		_id = id;
	}
	
	public void setIp(String ip) {
		_ipAddr = ip;
	}
	
	public void setPort(int port) {
		_port = port;
	}
	
	// Private members.
	private String _id;
	private String _ipAddr;
	private int _port;
}
