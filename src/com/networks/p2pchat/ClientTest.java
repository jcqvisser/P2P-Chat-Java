package com.networks.p2pchat;

import java.io.*;
import java.net.*;

public class ClientTest {

	public static void main(String argv[]) throws Exception{
		String sentence = "";
		BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
		Socket clientSocket = new Socket("localhost", 1337);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		while(sentence.compareTo("billeh") != 0) {
			sentence = inFromUser.readLine();
			outToServer.writeBytes(sentence + '\n');
		}
		clientSocket.close();
	}
}
