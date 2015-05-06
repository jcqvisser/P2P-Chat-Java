package com.networks.p2pchat;

public interface ConnectionHandle {
	public void start(String threadname);
	public void close();
	public void connect(String ipAddr);
}
