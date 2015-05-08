package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.networks.p2pchat.Message;
import com.networks.p2pchat.Message.messageType;
import com.networks.p2pchat.Peer;

public class XmlTest {
	
	private static final String MESSAGE_XML = "./message-jaxb.xml";

	public XmlTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws JAXBException, IOException  {
// TODO More testing is neccesary
		ArrayList<Peer> peers = new ArrayList<Peer>();
		
		Peer peer0 = new Peer("Jeff", "192.168.0.1", 1337);
		Peer peer1 = new Peer("Billeh", "192.168.0.5", 1337);
		peers.add(peer0);
		peers.add(peer1);
		
		Message msg = new Message();
		msg.setMessageType(messageType.HELO);
		msg.setOrigin(peer0);
		msg.setDestination(peer1);
		msg.setSource(peer0);
		msg.setUsers(peers);
		msg.setTtl(10);
		msg.setChannelID("fdsa");
		
	    JAXBContext context = JAXBContext.newInstance(Message.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	    
//	    m.marshal(msg, System.out);
//	    msg.send(System.out);
	    
	    OutputStream out = new FileOutputStream("C:\\Users\\jcqvi_000\\Desktop\\billeh.txt");
	    msg.send(out);
	    
	    InputStream in = new FileInputStream("C:\\Users\\jcqvi_000\\Desktop\\billeh.txt");
	    
	    Message msg1 = new Message(in);
	    msg1.send(System.out);
	}

}
