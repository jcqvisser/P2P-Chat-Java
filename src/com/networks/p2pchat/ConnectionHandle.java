package com.networks.p2pchat;

// Connection handle interface to make UDP and TCP connections 
// more interchangable.
public interface ConnectionHandle {
	public void start(String threadname);
	public void close();
	public void connect(String ipAddr, int port, String channel);
}
