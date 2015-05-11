package com.networks.p2pchat;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
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
		_clientChatHandle = new ClientChatThreadTCP(this, _clientSocket);
//		_messageText = "";
		launchWindow();
		try {
			_serverOutput = new DataOutputStream(_clientSocket.getOutputStream());
		} catch(IOException e) {
			System.err.println("Could not create client side output stream to server.");
		}
		_runThread = true;
		start();
	}
	
	public void run() {
		while(_runThread) {
			synchronized(this) {
				try {
					this.wait();
					try {
						//_serverOutput.writeBytes(createSendMessage(_messageText) + '\n');
// added next line
// TODO handle JAXB Exception
						_msg.send(_serverOutput);
					} catch (IOException ioe) {
						System.err.println("Couldn't send message to server." + ioe.getMessage());
						passClose();
					} catch (JAXBException e) {
						System.err.println("something went wrong with JAXB: " + e.getMessage());
					}
				} catch(InterruptedException e) {
					System.out.println("Wait interrupt thrown:" + e.getMessage());
				}
				
			}
//			if(_messageText.compareTo("QUIT") == 0) {
			if(_msg.getText().compareTo("QUIT") == 0){
				passClose();
			}
		}
	}
	
	public void sendMessage(String message) {
		synchronized(this) {
			//_messageText = message;
// added next 10 lines
// TODO this should be in create send message
			_msg = new Message(MessageType.MSG,
				new Peer("name",
						_clientSocket.getLocalAddress().toString(),
						_clientSocket.getLocalPort()),
				new Peer("servername",
						_serverIP,
						_serverPort),
				"ChannelID123",
				message
				);
			this.notify();
		}
	}
	
	public String getClientIPPort() {
		return _clientSocket.getLocalAddress().toString() + ":" + Integer.toString(_clientSocket.getLocalPort());
	}
	
	public void messageHandle(String message) {
		_chatWindow.displayMessage(message);
	}
	
	public synchronized void passClose() {
		_clientHandler.closeClientSocket(getClientIPPort());
	}
	
	public void close() {
		try {
			System.out.println("Closing client socket for: " + getClientIPPort());
			if(_clientChatHandle != null)
				_clientChatHandle.close();
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
	
	private String createSendMessage(String message) {
		String sendMessage = "";
		sendMessage += "{channel:" + _channel + ",message:" + message + "}";
		return sendMessage;
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
							sendMessage("QUIT");
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
	private ClientChatThreadTCP _clientChatHandle;
	private ClientHandleTCP _clientHandler;
	private volatile boolean _runThread;
	private ClientWindow _chatWindow;
	private String _channel;
	private Thread _thread;
	private String _serverIP;
	private int _serverPort;
	private DataOutputStream _serverOutput;
	//private String _messageText;
	private Message _msg;
}
