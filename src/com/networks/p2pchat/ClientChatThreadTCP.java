package com.networks.p2pchat;

import java.io.IOException;

import javax.xml.bind.JAXBException;

public class ClientChatThreadTCP implements Runnable {
	// Constructor, takes the holding class and messaging service object (linked to the socket).
	public ClientChatThreadTCP(ClientThreadTCP clientHandler, MessageService messageService) {
		_messageService = messageService;
		_clientHandler = clientHandler;
		_runClientChatThread = true;
		start();
	}
	
	// Close the thread that handles listening for the client.
	public void close() {
		_runClientChatThread = false;
	}
	
	// Thread run loop, listens for incoming messages from the server and then handles
	// them accordingly.
	public void run() {
		Message message;
		while(_runClientChatThread) {
			try {
				message = _messageService.receiveMessage();
				_clientHandler.messageHandle(message);
			} catch (IOException ioe) {
				// If the socket is closed, close the client
				// Give an error output if another error has occured.
				if(ioe.getMessage().compareTo("Socket closed") == 0){
					_clientHandler.passClose();
				} else {
					System.err.println("Couldn't read message from server: " + ioe.getMessage());
				}
			} catch (JAXBException jaxbe) {
				System.err.println("Could not parse XML: " + jaxbe);
			}
		}
	}
	
	// Private
	
	// Start the thread object, called by constructor.
	private void start() {
		if (_thread == null){
			_thread = new Thread (this);
			_thread.start ();
		}
	}
	
	// Private data members.
	private ClientThreadTCP _clientHandler;
	private volatile boolean _runClientChatThread;
	private MessageService _messageService;
	private Thread _thread;
}
