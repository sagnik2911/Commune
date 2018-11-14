package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import connection.ServerChatConnection;

public class TextChatUI implements KeyListener{

	JFrame frame;
	private JTextArea myChat;
	private JTextPane textPane;
	private JScrollPane textScroll;
	private ServerChatConnection chatConn;
	private JButton btnSend;
	private String to;
	

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					//ServerChatConnection.connectUser("pal","12345");
//					TextChatUI window = new TextChatUI("pal@darkdragon");
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}  //commented as this class is called from mainwindow #skd

	/**
	 * Create the application.
	 */
	public TextChatUI(String to) {
		chatConn = new ServerChatConnection();
		this.to = to;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Chat with "+to);
		frame.setBounds(100, 100, 660, 420);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
				
		createMenuBar();
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setContentType("text/html");
		
		chatConn.setOutputTextArea(textPane);
		chatConn.getChatHistory();
		
		textScroll = new JScrollPane(textPane);
		textScroll.setBounds(10, 31, 466, 241);
		frame.getContentPane().add(textScroll);
			
		myChat = new JTextArea();
		myChat.setLineWrap(true);
		myChat.setWrapStyleWord(true);
		JScrollPane myChatScroll = new JScrollPane(myChat);
		myChat.addKeyListener(this);
		myChatScroll.setBounds(10, 285, 466, 59);
		frame.getContentPane().add(myChatScroll);
		
		btnSend = new JButton("SEND");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String text=myChat.getText();
				/*SimpleDateFormat time = new SimpleDateFormat("(hh:mm:ss a)");
				text.replaceAll("[\\u0001-\\u0008\\u000B-\\u001F]", "");
				//textArea.append(time.format(new java.util.Date())+chatConn.getLoggedInUser()+": "+text+"\n");
				text = time.format(new java.util.Date())+chatConn.getLoggedInUser()+": "+text+"<br>";
			    String html_txt = "<div style='background-color: #349e92;border: 1px dotted black;'><b style='color: white;'>" + text + "</b></div>";*/
				String finaltext = textPane.getText().trim().replaceFirst("</body>", chatConn.makeMsg(chatConn.getLoggedInUser(),text) + "</body>");
				textPane.setText(finaltext);
				myChat.setText("");
				chatConn.sendMessage(to,text);
			}
		});
		btnSend.setBounds(10, 347, 89, 23);
		frame.getContentPane().add(btnSend);
		
		JButton btnReset = new JButton("RESET");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				myChat.setText(" ");
			}
		});
		btnReset.setBounds(116, 347, 89, 23);
		frame.getContentPane().add(btnReset);
				
		JComboBox<String> presenceMode = new JComboBox<String>();
		presenceMode.setToolTipText("set your presence mode");
		presenceMode.setBounds(514, 35, 105, 25);
		presenceMode.addItem("Online");
		presenceMode.addItem("Busy");
		presenceMode.addItem("Away");
		frame.getContentPane().add(presenceMode);
		
		presenceMode.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if(event.getStateChange()==ItemEvent.SELECTED);
				{
					String mode=(String)event.getItem();
					chatConn.setMyPresence(mode);
				}
			}
		});

	}

	
	private void createMenuBar() {
		JMenuBar menubar = new JMenuBar();
		menubar.setBounds(0, 0, 648, 23);
		
		JMenu options = new JMenu("Options");
		
		JMenuItem logoutMI = new JMenuItem("Logout");
		logoutMI.setToolTipText("Log out of your chat session");
		logoutMI.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chatConn.saveChat();
				chatConn.disconnect();
				System.out.println("Disconnected");
				System.exit(0);
			}
		});
		
		JMenuItem saveChatMI = new JMenuItem("Save Chat");
		saveChatMI.setToolTipText("Saves your Chat History");
		saveChatMI.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chatConn.saveChat();
			}
		});
		
		options.add(saveChatMI);
		options.add(logoutMI);
		
		menubar.add(options);
		
		frame.getContentPane().add(menubar);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == '\n'){
			e.consume();
			btnSend.doClick();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// i dont need it
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// leave it no need
		
	}
}
