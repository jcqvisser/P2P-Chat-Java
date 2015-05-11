package com.networks.p2pchat;

import java.io.IOException;

import javax.xml.bind.JAXBException;

public class ClientChatThreadTCP implements Runnable {
	public ClientChatThreadTCP(ClientThreadTCP clientHandler, MessageService messageService) {
		_messageService = messageService;
		_clientHandler = clientHandler;
		_runClientChatThread = true;
		start();
	}
	
	public void start() {
		if (_thread == null)
		{
			_thread = new Thread (this);
			_thread.start ();
		}
	}
	
	public void close() {
		_runClientChatThread = false;
	}
	
	public void run() {
		Message message;
		while(_runClientChatThread) {
			try {
				message = _messageService.receiveMessage();
				_clientHandler.messageHandle(message);
			} catch (IOException ioe) {
				System.err.println("Couldn't read message from server: " + ioe.getMessage());
				_clientHandler.passClose();
			} catch (JAXBException jaxbe) {
				System.err.println("Could not parse XML: " + jaxbe);
			}
		}
		System.out.println("Client thread closing");
	}
	
	// Private
	
	private ClientThreadTCP _clientHandler;
	private volatile boolean _runClientChatThread;
	private MessageService _messageService;
	private Thread _thread;
}
