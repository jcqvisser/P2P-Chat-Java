/**
 * 
 */
package com.networks.p2pchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * A receiver object listens on a stream for new Messages,
 * when one is received, the message is passed to the conversation object that is holding the Receiver
 * 
 * @author jcqvi_000
 *
 */
public class Receiver implements Runnable{

	/**
	 * Standard constructor
	 * Takes a Conversation object to pass new messages on to and
	 * an InputStream object on which to listen for new messages
	 */
	public Receiver(InputStream inStream, Conversation conv) {
		_conv = conv;
		_inStream = inStream;
		_runThread = false;
		_inputReader = new BufferedReader(new InputStreamReader(_inStream));	
	}
	
	/* 
	 * Main run-loop which waits around for messages to be send to it through the inputStream
	 * @see java.lang.Runnable#run()
	 * 
	 * messages are received line by line as xml, this is unmarshalled and a Message object is created once
	 * the closing tag of the XML is read
	 */
	@Override
	public void run() {
		_runThread = true;
		while (_runThread) {
			String xml = "";
			String line = "";
			
			while (line.compareTo("</ns2:message>") != 0) {
				try {
					line = _inputReader.readLine().toString();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				xml = xml + line;
			}
		    StringReader reader1 = new StringReader(xml);
		       
		    JAXBContext context;
		    Unmarshaller um = null;
			try {
				context = JAXBContext.newInstance(com.networks.p2pchat.Message.class);
                um = context.createUnmarshaller();	
				_conv.handleMessage((Message) um.unmarshal(reader1));			
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close(){
		_runThread = false;
	}
	
	/*The Conversation object to pass received mssages to. This object is also intended to hold this Receiver. */
	private Conversation _conv;
	
	/*The inputStream on which to listen for new messages*/
	private InputStream _inStream;
	
	/* boolean value to see whether this thread must be closed*/
	private volatile boolean _runThread;
	
	/* BufferedReader for reading from InputStream line-by-line*/
	private BufferedReader _inputReader;
}
