package com.networks.p2pchat;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.sun.glass.events.WindowEvent;

public class ClientThreadTCP implements Runnable {

	public ClientThreadTCP(ConnectionHandleTCP connectionHandler, Socket clientSocket) {
		_clientSocket = clientSocket;
		_connectionHandler = connectionHandler;
		_serverIP = _clientSocket.getInetAddress().toString();
		_serverPort = _clientSocket.getPort();
		_clientChatHandle = new ClientChatThreadTCP(this, _clientSocket);
		_messageText = "";
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
		String userInput = "";
		while(_runThread) {
//			try {
//				userInput = _inFromUser.readLine();
//			} catch (IOException ioe) {
//				System.err.println("Couldn't recieve user input in thread: " + _serverIP + " - " + ioe.getMessage());
//			}
			synchronized(this) {
				try {
					this.wait();
					try {
						_serverOutput.writeBytes(_messageText + '\n');
					} catch (IOException ioe) {
						System.err.println("Couldn't send message to server." + ioe.getMessage());
						passClose();
					}
				} catch(InterruptedException e) {
					System.out.println("Wait interrupt thrown:" + e.getMessage());
				}
				
			}
			if(userInput.compareTo("!") == 0) {
				passClose();
			}
		}
	}
	
	public void sendMessage(String message) {
		synchronized(this) {
			_messageText = message;
			this.notify();
		}
	}
	
	public String getClientIPPort() {
		return _clientSocket.getLocalAddress().toString() + ":" + Integer.toString(_clientSocket.getLocalPort());
	}
	
	public void messageHandle(String message) {
		System.out.println(message);
		_chatWindow.displayMessage(message);
	}
	
	public synchronized void passClose() {
		_connectionHandler.closeClientSocket(getClientIPPort());
	}
	
	public void close() {
		try {
			System.out.println("Closing client socket for: " + getClientIPPort());
			_runThread = false;
			if(_clientChatHandle != null)
				_clientChatHandle.close();
			if(_clientSocket != null)
				_clientSocket.close();
		} catch (IOException e) {
			System.err.println("Could not close client socket");
		}
	}
	
	private void start() {
		if (_thread == null)
		{
			System.out.println("Client connected to: " + _serverIP + ":" + Integer.toString(_serverPort));
			_thread = new Thread (this, _serverIP);
			_thread.start ();
		}
	}
	
	
	// Private members
	
	public void launchWindow() {
		ClientThreadTCP tempThis = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					_chatWindow = new ClientWindow(tempThis, "title");
					_chatWindow.setVisible(true);
					_chatWindow.addWindowListener(new WindowAdapter() {
						@SuppressWarnings("unused")
						public void windowClosing(WindowEvent e) {
							passClose();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private Socket _clientSocket;
	private ClientChatThreadTCP _clientChatHandle;
	private ConnectionHandleTCP _connectionHandler;
	private volatile boolean _runThread;
	private ClientWindow _chatWindow;
	private Thread _thread;
	private String _serverIP;
	private int _serverPort;
	private DataOutputStream _serverOutput;
	private String _messageText;
}
