package com.networks.p2pchat;

import java.net.Socket;

public class ClientChatThreadTCP {
	public ClientChatThreadTCP(ClientThreadTCP clientHandler, Socket clientSocket) {
		_clientSocket = clientSocket;
		_clientHandler = clientHandler;
	}
	
	Socket _clientSocket;
	ClientThreadTCP _clientHandler;
}
