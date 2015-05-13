/**
 * 
 */
package com.networks.p2pchat;

import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


/**
 * Sender object to which MEssage objects are passed in order to send them over an OutputStream.
 * Message objects are unmarshalled into XML and sent.
 * @author jcqvi_000
 *
 */
public class Sender {

	/**
	 * Standard constructor
	 * takes an OutputStream on which to write messages 
	 */
	public Sender(OutputStream outStream) {
		_outStream = outStream;
	}
	
	/* function used to send message over the OutputStream associated with this object.*/
	public boolean sendMessage(Message msg) {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(Message.class);
			Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(msg, _outStream);
        } 
		catch (JAXBException e) {
			e.printStackTrace();
		}

		return true;
	}
	
	/* OutputStream on which to write messages*/
	private OutputStream _outStream;

}
