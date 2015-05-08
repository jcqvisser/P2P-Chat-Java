package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.networks.p2pchat.Message;
import com.networks.p2pchat.Message.FileType;
import com.networks.p2pchat.Message.MessageType;
import com.networks.p2pchat.Peer;

public class XmlTest {
	
	private static final String MESSAGE_XML = "./message-jaxb.xml";

	public XmlTest() {
	}

	public static void main(String[] args) throws JAXBException, IOException  {
// TODO More testing is neccesary
		ArrayList<Peer> peers = new ArrayList<Peer>();
		
		Peer peer0 = new Peer("Jeff", "192.168.0.1", 1337);
		Peer peer1 = new Peer("Billeh", "192.168.0.5", 1337);
		peers.add(peer0);
		peers.add(peer1);
		
		ArrayList<String> channels = new ArrayList<String>();
		channels.add("channel_0");
		channels.add("channel_1");	
		
		Message msg0 = new Message();
		msg0.setMessageType(MessageType.HELO);
		msg0.setTtl(10);
		msg0.setSource(peer0);
		msg0.setOrigin(peer0);
		msg0.setDestination(peer1);
		msg0.setChannelID("thisisthechannelid");
		msg0.setText("thisisthemessage");
		msg0.setFilename("FiLeNaMe");
		msg0.setData("f".getBytes());
		msg0.setChannels(channels);
		msg0.setForwardable(true);
		msg0.setUsers(peers);
		msg0.setChannelID("fdsa");
		msg0.setFileType(FileType.JPG);
		
	    JAXBContext context = JAXBContext.newInstance(Message.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	    
	    OutputStream out = new FileOutputStream("C:\\Users\\jcqvi_000\\Desktop\\billeh.txt");
	    msg0.send(out);
	    msg0.send(System.out);
	    
	    InputStream in = new FileInputStream("C:\\Users\\jcqvi_000\\Desktop\\billeh.txt");
	    
	    Message msg1 = new Message(in);
	    msg1.send(System.out);
	}
}
