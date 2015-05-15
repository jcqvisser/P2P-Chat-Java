package com.networks.p2pchat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ConnectionWindow extends JFrame implements Runnable {
	/**
	 * Create the frame.
	 * @param graphicInterface
	 */
	public ConnectionWindow(GraphicInterface graphicInterface) {
		_graphicInterface = graphicInterface;
		_channels = new HashMap<String, DefaultListModel<String>>();
		
		_addressBook = AddressBook.getInstance();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 518, 347);
		setTitle("Connection Window");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		/*
		 * Initialize the text panes.
		 */
		
		txtpnUser = new JTextPane();
		StyledDocument doc = txtpnUser.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		txtpnUser.setText("Username (IP Address):");
		txtpnUser.setEditable(false);
		txtpnUser.setBounds(10, 11, 236, 20);
		contentPane.add(txtpnUser);
		
		txtpnChannel = new JTextPane();
		StyledDocument docCh = txtpnChannel.getStyledDocument();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		docCh.setParagraphAttributes(0, docCh.getLength(), center, false);
		txtpnChannel.setText("Channel:");
		txtpnChannel.setEditable(false);
		txtpnChannel.setBounds(256, 11, 236, 20);
		contentPane.add(txtpnChannel);
		
		txtpnAddChannel = new JTextPane();
		StyledDocument docACh = txtpnAddChannel.getStyledDocument();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		docACh.setParagraphAttributes(0, docACh.getLength(), center, false);
		txtpnAddChannel.setText("Create Channel:");
		txtpnAddChannel.setEditable(false);
		txtpnAddChannel.setBounds(10, 280, 136, 20);
		contentPane.add(txtpnAddChannel);
		
		/*
		 * Initialize the scroll pane objects.
		 */
		
		scrollPaneUser = new JScrollPane();
		scrollPaneUser.setBounds(10, 42, 236, 227);
		contentPane.add(scrollPaneUser);
		
		scrollPaneChannel = new JScrollPane();
		scrollPaneChannel.setBounds(256, 42, 236, 187);
		contentPane.add(scrollPaneChannel);
		
		/*
		 * Initialize the list objects.
		 */
		
		lstChannel = new JList<String>();
		lstChannel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("Test " + lstChannel.getSelectedIndex()) ;
			}
		});
		lstChannel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneChannel.setViewportView(lstChannel);
		
		lstUser = new JList<String>();
		lstUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				System.out.println("test2 " + lstUser.getSelectedIndex());
			}
		});
		lstUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		modelUser = new DefaultListModel<String>();
		lstUser.setModel(modelUser);
		scrollPaneUser.setViewportView(lstUser);
		
		/*
		 * Initialize the button.
		 */
		
		btnAddChannel = new JButton("Private Conversation");
		btnAddChannel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnAddChannel.setBounds(256, 240, 236, 29);
		contentPane.add(btnAddChannel);
		
		/*
		 * Initialize the new channel text field.
		 */
		
		textField = new JTextField();
		textField.setText("");
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					// Handle enter command.
				}
			}
		});
		textField.setBounds(156, 280, 336, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		start();
	}
	
	public void updateUserList() {
		modelUser.clear();
		for(Entry<String, String> user : _addressBook.getMap().entrySet()) {
			modelUser.addElement(user.getValue() + " (" + user.getKey() + ")");
			if(!_channels.containsKey(user.getKey())) {
				_channels.put(user.getKey(), new DefaultListModel<String>());
			}
		}
	}
	
	public void updateChannelList(String userIp, ArrayList<String> channels) {
		_channels.get(userIp).clear();
		for(String channel : channels) {
			_channels.get(userIp).addElement(channel);
		}
	}
	
	/**
	 * Start the thread that handles the client window.
	 */
	public void start() {
		if (_thread == null)
		{
			_thread = new Thread (this, "ConnectionWindow:");
			_thread.start ();
		}
	}
	
	/**
	 * The thread run loop, is called when the thread starts running.
	 */
	public void run() {
		try {
			setVisible(true);
			toFront();
			repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The content pane holds all of the other displayable content.
	 */
	private JPanel contentPane;
	/**
	 * The thread object is used for threading the connection window.
	 */
	private Thread _thread;
	/** 
	 * An instance of graphic interface is stored for passing information
	 * to and from the window
	 */
	private GraphicInterface _graphicInterface;
	/**
	 * Serial for the window.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The text pane shows the user information.
	 */
	private JTextPane txtpnUser;
	/**
	 * The text pane shows the channel information.
	 */
	private JTextPane txtpnChannel;
	/**
	 * 
	 */
	private JTextPane txtpnAddChannel;
	/**
	 * Scrollpane for user view.
	 */
	private JScrollPane scrollPaneUser;
	/**
	 * Scrollpane for channel view.
	 */
	private JScrollPane scrollPaneChannel;
	/**
	 * List of users.
	 */
	private JList<String> lstUser;
	private DefaultListModel<String> modelUser;
	/**
	 * List of channels.
	 */
	private JList<String> lstChannel;
	/**
	 * The text input for creating new channels.
	 */
	private JTextField textField;
	/**
	 * 
	 */
	private JButton btnAddChannel;
	/**
	 * 
	 */
	private AddressBook _addressBook;
	/**
	 * 
	 */
	private Map<String, DefaultListModel<String>> _channels;
}
