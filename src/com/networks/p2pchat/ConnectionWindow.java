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
		_nameIpMap = new HashMap<String, String>();
		
		_addressBook = AddressBook.getInstance();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		setBounds(100, 100, 518, 419);
		setTitle("Connection Window");
		_contentPane = new JPanel();
		_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(_contentPane);
		_contentPane.setLayout(null);
		
		/*
		 * Initialize the text panes.
		 */
		
		_txtpnUser = new JTextPane();
		StyledDocument doc = _txtpnUser.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		_txtpnUser.setText("Username (IP Address):");
		_txtpnUser.setEditable(false);
		_txtpnUser.setBounds(10, 11, 236, 20);
		_contentPane.add(_txtpnUser);
		
		_txtpnChannel = new JTextPane();
		StyledDocument docCh = _txtpnChannel.getStyledDocument();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		docCh.setParagraphAttributes(0, docCh.getLength(), center, false);
		_txtpnChannel.setText("Channel:");
		_txtpnChannel.setEditable(false);
		_txtpnChannel.setBounds(256, 11, 236, 20);
		_contentPane.add(_txtpnChannel);
		
		_txtpnAddChannel = new JTextPane();
		StyledDocument docACh = _txtpnAddChannel.getStyledDocument();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		docACh.setParagraphAttributes(0, docACh.getLength(), center, false);
		_txtpnAddChannel.setText("Create Channel:");
		_txtpnAddChannel.setEditable(false);
		_txtpnAddChannel.setBounds(10, 280, 136, 20);
		_contentPane.add(_txtpnAddChannel);
		
		_textPaneOwnedCh = new JTextPane();
		StyledDocument docAChOwn = _textPaneOwnedCh.getStyledDocument();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		docAChOwn.setParagraphAttributes(0, docAChOwn.getLength(), center, false);
		_textPaneOwnedCh.setText("Owned Channels:");
		_textPaneOwnedCh.setEditable(false);
		_textPaneOwnedCh.setBounds(10, 311, 136, 68);
		_contentPane.add(_textPaneOwnedCh);
		
		/*
		 * Initialize the scroll pane objects.
		 */
		
		_scrollPaneUser = new JScrollPane();
		_scrollPaneUser.setBounds(10, 42, 236, 227);
		_contentPane.add(_scrollPaneUser);
		
		_scrollPaneChannel = new JScrollPane();
		_scrollPaneChannel.setBounds(256, 42, 236, 187);
		_contentPane.add(_scrollPaneChannel);
		
		_scrollPaneOwnedCh = new JScrollPane();
		_scrollPaneOwnedCh.setBounds(156, 311, 336, 68);
		_contentPane.add(_scrollPaneOwnedCh);
		
		/*
		 * Initialize the list objects.
		 */
		
		// Initialize channel list.
		_lstChannel = new JList<String>();
		_lstChannel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) {
				if(_nameIpMap.get(_lstUser.getSelectedValue()) != null && _lstChannel.getSelectedValue() != null) {
					/*
					 * Open a new channel window when the double click command is given.
					 */
					if(evt.getClickCount() == 2) {
						_graphicInterface.sendJOIN(_nameIpMap.get(_lstUser.getSelectedValue()), _lstChannel.getSelectedValue());
					}
				}
			}
		});
		_lstChannel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_scrollPaneChannel.setViewportView(_lstChannel);
		
		// Initialize user list
		_lstUser = new JList<String>();
		_lstUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) {
				if(_nameIpMap.get(_lstUser.getSelectedValue()) != null) {
					_lstChannel.setModel(_channels.get(_nameIpMap.get(_lstUser.getSelectedValue())));
					_graphicInterface.sendLISTCH(_nameIpMap.get(_lstUser.getSelectedValue()));
					
					if(evt.getClickCount() == 2) {
						_graphicInterface.sendJOIN(_nameIpMap.get(_lstUser.getSelectedValue()), "private");
					}
				} 
			}
		});
		_lstUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_modelUser = new DefaultListModel<String>();
		_lstUser.setModel(_modelUser);
		_scrollPaneUser.setViewportView(_lstUser);
		
		// Initialize owned channel list.
		_modelOwnedCh = new DefaultListModel<String>();
		_lstOwnedCh = new JList<String>();
		_scrollPaneOwnedCh.setViewportView(_lstOwnedCh);
		
		/*
		 * Initialize the button.
		 */
		
		_btnAddChannel = new JButton("Private Conversation");
		_btnAddChannel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(_nameIpMap.get(_lstUser.getSelectedValue()) != null) {
					_graphicInterface.sendJOIN(_nameIpMap.get(_lstUser.getSelectedValue()), "private");
				}
			}
		});
		_btnAddChannel.setBounds(256, 240, 236, 29);
		_contentPane.add(_btnAddChannel);
		
		/*
		 * Initialize the new channel text field.
		 */
		
		_textField = new JTextField();
		_textField.setText("");
		_textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					// Handle enter command.
					_graphicInterface.createChannel(_textField.getText());
					_textField.setText("");
				}
			}
		});
		_textField.setBounds(156, 280, 336, 20);
		_contentPane.add(_textField);
		_textField.setColumns(10);
		
		
		start();
	}
	
	/**
	 * Call this function when a new user is registered on the network, adds them
	 * to the list of online users for connecting to.
	 */
	public void updateUserList() {
		_modelUser.clear();
		for(Entry<String, String> user : _addressBook.getMap().entrySet()) {
			String listEntry = user.getValue() + " (" + user.getKey() + ")";
			_modelUser.addElement(listEntry);
			_nameIpMap.put(listEntry, user.getKey());
			if(!_channels.containsKey(user.getKey())) {
				_channels.put(user.getKey(), new DefaultListModel<String>());
			}
		}
	}
	
	/**
	 * Update the list of channels after a response is heard from the target
	 * ip that the request was made to.
	 * @param userIp
	 * @param channels
	 */
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
	private JPanel _contentPane;
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
	private JTextPane _txtpnUser;
	/**
	 * The text pane shows the channel information.
	 */
	private JTextPane _txtpnChannel;
	/**
	 * 
	 */
	private JTextPane _textPaneOwnedCh;
	/**
	 * 
	 */
	private JTextPane _txtpnAddChannel;
	/**
	 * Scrollpane for user view.
	 */
	private JScrollPane _scrollPaneUser;
	/**
	 * Scrollpane for channel view.
	 */
	private JScrollPane _scrollPaneChannel;
	/**
	 * 
	 */
	private JScrollPane _scrollPaneOwnedCh;
	/**
	 * List of users.
	 */
	private JList<String> _lstUser;
	private DefaultListModel<String> _modelUser;
	/**
	 * List of channels.
	 */
	private JList<String> _lstChannel;
	private Map<String, DefaultListModel<String>> _channels;
	/**
	 * 
	 */
	private JList<String> _lstOwnedCh;
	private DefaultListModel<String> _modelOwnedCh;
	/**
	 * The text input for creating new channels.
	 */
	private JTextField _textField;
	/**
	 * 
	 */
	private JButton _btnAddChannel;
	/**
	 * 
	 */
	private AddressBook _addressBook;
	/**
	 * 
	 */
	private Map<String, String> _nameIpMap;
}
