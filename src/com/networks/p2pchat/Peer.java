package com.networks.p2pchat;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Peer class that represents a user on the network.
 * Holds an ID (String) and IP (String) for the user. 
 * @author jcqvi_000
 * @author Anthony
 *
 */
@XmlRootElement( name = "peer")
public class Peer {
	
	/* Standard constructor, takes an ID (STRING) and IP (String) */
	public Peer(String id, String ipAddr) {
		_id = id;
		_ipAddr = ipAddr;
	}

	/* No arguments constructor, used for JAXB Marshalling*/
	public Peer() {}
	
	/* Function to get the ID String, used for JAXB Marshalling*/
	public String getId() {
		return _id;
	}
	
	/* Function to get IP String, used in JAXB Marshalling*/
	public String getIp() {
		return _ipAddr;
	}
	
	/*Function to set the ID String, used for JAXB Unmarshalling*/
	public void setId(String id) {
		_id = id;
	}
	
	/* Function for setting the IP String, used in JAXB Unmarshalling*/
	public void setIp(String ip) {
		_ipAddr = ip;
	}
	
	/* ID for user, a string*/
	private String _id;
	
	/* IP for user IP, a string*/
	private String _ipAddr;
}
