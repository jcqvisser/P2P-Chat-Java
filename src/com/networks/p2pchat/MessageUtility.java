package com.networks.p2pchat;

public final class MessageUtility {
	public MessageUtility(String selfIP, int selfPort) {
		_selfIP = selfIP;
		_selfPort = selfPort;
	}
	
	public static final String makeMessage(	String msgType, 
											int ttl, 
											String source, 
											int sourcePort,
											String destination, 
											int destinationPort,
											String originalSource,
											int originalSourcePort,
											String originalDestination,
											int originalDestinationPort,
											String text,
											String filename) {
		String msg = "";
		return msg;
	}
	
	private String _selfIP;
	private int _selfPort;

}
