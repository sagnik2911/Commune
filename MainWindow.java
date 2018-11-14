package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.jtattoo.plaf.noire.NoireLookAndFeel;

import connection.RosterManager;
import connection.ServerChatConnection;
import connection.TheDamnBrowser;

public class MainWindow {

	private JFrame frame;
	private JLabel statusLabel;
	private JDialog login;
	private RosterManager rosterMgr;
	private ServerChatConnection chatConn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					/*
					 * Selecting LookAndFeel using JTattoo
					 */
					Properties themeprop = new Properties();
					themeprop.setProperty("logoString", "");// removing the logo
															// from menu
					NoireLookAndFeel.setCurrentTheme(themeprop);
					UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Commune");
		frame.setBounds(100, 100, 255, 468);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		URL url = MainWindow.class.getResource("/res/logo.jpg");
		ImageIcon logo = new ImageIcon(url);
		frame.setIconImage(logo.getImage());

		login = new LoginDialog(frame, true);
		login.setVisible(true);

		chatConn = new ServerChatConnection();
		rosterMgr = chatConn.getRosterManager(); // After
																	// connect
																	// has been
																	// called in
																	// LoginDialog

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnCommune = new JMenu("Commune");
		menuBar.add(mnCommune);

		JMenuItem mntmSetStatus = new JMenuItem("Set Status");
		mntmSetStatus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String status = JOptionPane.showInputDialog("Enter your status message: ");
				rosterMgr.setStatus(status);
				statusLabel.setText("Status Message : "+status);
			}
		});
		mnCommune.add(mntmSetStatus);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnCommune.add(mntmExit);

		JMenu mnContacts = new JMenu("Contacts");
		menuBar.add(mnContacts);

		JMenuItem mntmAddContact = new JMenuItem("Add Contact");
		mntmAddContact.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				boolean status = false;
				String name = JOptionPane.showInputDialog("Enter full name of user: [user@server]");
				String nickname = JOptionPane.showInputDialog("Enter nickname for user");
				if ((name != null) && !(name.isEmpty())) {
					status = rosterMgr.addFriend(name, nickname);
				} else {
					JOptionPane.showMessageDialog(frame, "Name cannot be empty!", "No user", JOptionPane.ERROR_MESSAGE);
				}
				if(status)
					JOptionPane.showMessageDialog(frame, "Added Successfully");
				else
					JOptionPane.showMessageDialog(frame, "Sorry ! not done");
			}
		});
		mnContacts.add(mntmAddContact);

		JMenuItem mntmSearchUser = new JMenuItem("Search User By Name");
		mntmSearchUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String uname = JOptionPane.showInputDialog("Enter name of user : ");
				chatConn.searchUser(uname);
			}
		});
		mnContacts.add(mntmSearchUser);

		JMenu mnActions = new JMenu("Actions");
		menuBar.add(mnActions);

		JMenuItem mntmStartAText = new JMenuItem("Start a text chat");
		mntmStartAText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String to = JOptionPane.showInputDialog("Enter full id of Receipent : ");
				if ((to != null) && !(to.isEmpty())) {
					TextChatUI window = new TextChatUI(to);
					window.frame.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(frame, "Name cannot be empty!", "No Receipent",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mnActions.add(mntmStartAText);

		JMenuItem mntmStartAnAudio = new JMenuItem("Start an Audio chat");
		mntmStartAnAudio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					String userPath = System.getProperty("user.home");
					System.out.println(userPath);
					InputStream is = MainWindow.class.getResourceAsStream("/res/VOIPCaller.exe");
					OutputStream os = new FileOutputStream(userPath + File.separator + "VOIPCaller.exe");

					byte[] b = new byte[2048];
					int length;
					while ((length = is.read(b)) != -1)
						os.write(b, 0, length);

					is.close();
					os.close();
					System.out.println("Copy Done");
					Runtime.getRuntime().exec("cmd.exe /c start " + userPath + File.separator + "VOIPCaller.exe");

				} catch (FileNotFoundException e) {
					System.out.println("File Not Found");
				} catch (IOException e) {
					System.out.println("IOException in Audio");
				}
			}
		});
		mnActions.add(mntmStartAnAudio);

		JMenuItem mntmStartAVideo = new JMenuItem("Start a Video chat");
		mntmStartAVideo.addActionListener(new TheDamnBrowser());
		mnActions.add(mntmStartAVideo);
		
		JPanel contentPanel = new JPanel();
		JPanel myInfoPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());

		myInfoPanel.setLayout(new BoxLayout(myInfoPanel, BoxLayout.Y_AXIS));
		
		myInfoPanel.setBackground(new Color(217, 236, 255));
		
		JLabel userLabel = new JLabel("Logged in : "+chatConn.getLoggedInUser());
		userLabel.setForeground(Color.BLACK);
		myInfoPanel.add(userLabel);
		
		statusLabel = new JLabel("Status Message : "+rosterMgr.getStatus(chatConn.getLoggedInUser()));
		statusLabel.setForeground(Color.BLACK);
		myInfoPanel.add(statusLabel);
		
		contentPanel.add(myInfoPanel, BorderLayout.NORTH);
		frame.add(contentPanel);
	}
}
