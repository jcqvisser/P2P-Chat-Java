package com.networks.p2pchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.Socket;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBContext;

/**
 * 
 * @author jcqvi_000
 * @deprecated use {@link #Sender} and {@link #Receiver} instead.
 */
@Deprecated
public class MessageService {

	public MessageService(Socket socket) {
		try {
			_outputStream = socket.getOutputStream();
			_inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * function moved
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 * @deprecated use {@link #com.networks.p2pchat.Sender} instead
	 */
	@Deprecated
	public Message receiveMessage() throws JAXBException, IOException {
		String xml = "";
		String line = "";
		
		while (line.compareTo("</ns2:message>") != 0) {
			line = _inputReader.readLine().toString();
				
			xml = xml + line;
		}
	    StringReader reader1 = new StringReader(xml);
	       
	    JAXBContext context;
	    Unmarshaller um = null;
		context = JAXBContext.newInstance(com.networks.p2pchat.Message.class);
		um = context.createUnmarshaller();	
		return (Message) um.unmarshal(reader1);
	}
		
	/**
	 * Function moved
	 * @param message
	 * @throws JAXBException
	 * @deprecated use {@link #com.networks.p2pchat.Receiver} instead
	 */
	@Deprecated
	public void sendMessage(Message message) throws JAXBException {
	    JAXBContext context;
		context = JAXBContext.newInstance(Message.class);
		Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	    m.marshal(message, _outputStream);
	}
	
	/**
	 * Function must be moved, this class is gonna be ditched...
	 * @param msg
	 * @param destination
	 * @return
	 * @throws IllegalStateException
	 * @deprecated needs to be moved to post-office, or something. I dont know.
	 */
	@Deprecated
	public Message forward(Message msg, Peer destination) throws IllegalStateException{
		if (!msg.getForwardable()) throw new IllegalStateException();
		
		Message fwd =  new Message(msg);
		fwd.setSource(fwd.getDestination());
		fwd.setDestination(destination);
		return fwd;
	}
	
	private BufferedReader _inputReader;
	private OutputStream _outputStream;
}
