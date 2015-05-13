package com.networks.p2pchat;

import java.util.HashMap;
import java.util.Map;

/**
 * The addressbook contains all known people on the network, holding the
 * ip address and id of each person. This class is a singleton so that 
 * only one set of addresses can be accessed within the code.
 * The objects are stored as peers.
 * @author Anthony
 *
 */

public class AddressBook {
	
	/* Protected constructor, initializes the map object, cannot be called */
	protected AddressBook() {
		_addresses = new HashMap<String, String>();
	}
	
	/* return an instance of the static address book singleton */
	public static AddressBook getInstance() {
		if(_addressBookInstance == null) {
			_addressBookInstance = new AddressBook();
		}
		return _addressBookInstance;
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
	
	/* Returns the full map (for iterating through and getting all entries) */
	public Map<String, String> getMap() {
		return _addresses;
	}
	
	/* Check if the IP address exists in the hash table, i.e. does it have a value assigned */
	public boolean addressExists(String AddrIp) {
		return (_addresses.get(AddrIp) != null);
	}
	
	// Private member variables.
	
	/* Stores a map of IP addresses related to the ID's of the users. */
	Map<String, String> _addresses;
	
	/* Private static object for the addressbook instance singleton */
	private static AddressBook _addressBookInstance;
}
