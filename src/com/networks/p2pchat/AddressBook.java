package com.networks.p2pchat;

import java.util.HashMap;
import java.util.Map;

/**
 * The addressbook contains all known people on the network, holding the
 * ip address and id of each person.
 * The objects are stored as peers.
 * @author Anthony
 *
 */

public class AddressBook {
	
	/* Default constructor, initializes the map object */
	public AddressBook() {
		_addresses = new HashMap<String, String>();
	}
	
	/* Add a new key-value pair to the map, takes a Peer object */
	public void addAddress(Peer address) {
		_addresses.put(address.getIp(), address.getId());
	}
	
	/* Remove the key-value pair for AddrIp from the list */
	public void removeAddress(String AddrIp) {
		_addresses.remove(AddrIp);
	}
	
	/* Get the Peer for the relevant address key-value pair. */
	public Peer getAddress(String AddrIp) {
		if(_addresses.get(AddrIp) != null) {
			return new Peer(AddrIp, _addresses.get(AddrIp));
		} else {
			return null;
		}
	}
	
	/* Check if the IP address exists in the hash table, i.e. does it have a value assigned */
	public boolean addressExists(String AddrIp) {
		return (_addresses.get(AddrIp) != null);
	}
	
	// Private member variables.
	
	/* Stores a map of IP addresses related to the ID's of the users. */
	Map<String, String> _addresses;
}
