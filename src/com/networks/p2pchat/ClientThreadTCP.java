package com.networks.p2pchat;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

import javax.xml.bind.JAXBException;

import com.networks.p2pchat.Message.MessageType;

public class ClientThreadTCP implements Runnable {

	public ClientThreadTCP(ClientHandleTCP clientHandler, Socket clientSocket, String channel) {
		_channel = channel;
		_clientSocket = clientSocket;
		_clientHandler = clientHandler;
		_serverIP = _clientSocket.getInetAddress().toString();
		_serverPort = _clientSocket.getPort();
		_messageService = new MessageService(_clientSocket);
		_clientListenThread = new ClientChatThreadTCP(this, _messageService);
		_message = null;
		launchWindow();
		_runThread = true;
		start();
	}
	
	public void run() {
		while(_runThread) {
			synchronized(this) {
				try {
					this.wait();
					try {
						_messageService.sendMessage(_message);
					} catch (JAXBException e) {
						System.err.println("Error sending XML data: " + e);
					}
				} catch(InterruptedException e) {
					System.out.println("Wait interrupt thrown:" + e.getMessage());
				}
			}
		}
	}
	
	public void sendMessage(String message) {
		synchronized(this) {
			_message = createSendMessage(message);
			this.notify();
		}
	}
	
	public void sendMessage(Message message) {
		synchronized(this) {
			_message = message;
			this.notify();
		}
	}
	
	public String getClientIPPort() {
		return _clientSocket.getLocalAddress().toString() + ":" + Integer.toString(_clientSocket.getLocalPort());
	}
	
	public void messageHandle(Message message) {
		_chatWindow.displayMessage(message.getOrigin().getIp() + ":" + message.getOrigin().getPort() + ": " + message.getText());
	}
	
	public synchronized void passClose() {
		_clientHandler.closeClientSocket(getClientIPPort());
	}
	
	public void close() {
		try {
			System.out.println("Closing client socket for: " + getClientIPPort());
			if(_clientListenThread != null)
				_clientListenThread.close();
			if(_clientSocket != null)
				_clientSocket.close();
			if(_chatWindow != null)
				_chatWindow.dispose();
			_runThread = false;
		} catch (IOException e) {
			System.err.println("Could not close client socket");
		}
	}
	
	// Private members
	
	private void start() {
		if (_thread == null)
		{
			System.out.println("Client connected to: " + _serverIP + ":" + Integer.toString(_serverPort));
			_thread = new Thread (this, _serverIP);
			_thread.start ();
		}
	}
	
	private Message createSendMessage(String message) {
		return new Message(MessageType.MSG,
				new Peer("Test",
						_clientSocket.getLocalAddress().toString(),
						_clientSocket.getLocalPort()),
				new Peer("TestDest",
						_serverIP,
						_serverPort),
				"TestChannel",
				message);
	}
	
	
	public void launchWindow() {
		ClientThreadTCP tempThis = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					_chatWindow = new ClientWindow(tempThis, _channel + "-" + _serverIP + ":" + Integer.toString(_serverPort));
					_chatWindow.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							sendMessage(new Message(MessageType.QUIT,
									new Peer("TestId",
											_clientSocket.getLocalAddress().toString(),
											_clientSocket.getLocalPort()),
									new Peer("TestDestination",
											_serverIP,
											_serverPort),
									"TestChannelID"));
						}
					});
					_chatWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private Socket _clientSocket;
	private ClientChatThreadTCP _clientListenThread;
	private ClientHandleTCP _clientHandler;
	private MessageService _messageService;
	private volatile boolean _runThread;
	private ClientWindow _chatWindow;
	private String _channel;
	private Thread _thread;
	private String _serverIP;
	private int _serverPort;
	private Message _message;
}
