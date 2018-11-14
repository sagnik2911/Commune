package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import connection.ServerChatConnection;

public class LoginDialog extends JDialog {

	/**
	 * The default serial ID
	 */
	private static final long serialVersionUID = 1L;
	
	private final JLabel lbluname=new JLabel("Username");
	private final JLabel lblpass=new JLabel("Password");
	
	private final JTextField tfuname=new JTextField(15);
	private final JPasswordField pfpass=new JPasswordField();
	
	private final JButton btnOK=new JButton("Login");
	private final JButton btnRegister=new JButton("Register");
	private final JButton btnCancel=new JButton("Cancel");
	
	private final JLabel lblstatus= new JLabel(" ");
	
	public LoginDialog(final JFrame parent,boolean modal){
		super(parent,modal);
		
		JPanel p3 = new JPanel(new GridLayout(2,1));
		p3.add(lbluname);
		p3.add(lblpass);
		
		JPanel p4 = new JPanel(new GridLayout(2,1));
		p4.add(tfuname);
		p4.add(pfpass);
		
		JPanel p1=new JPanel();
		p1.add(p3);
		p1.add(p4);
		
		JPanel p2=new JPanel();
		p2.add(btnOK);
		p2.add(btnCancel);
		p2.add(btnRegister);
		
		JPanel p5= new JPanel(new BorderLayout());
		p5.add(p2,BorderLayout.CENTER);
		p5.add(lblstatus,BorderLayout.NORTH);
		lblstatus.setForeground(Color.RED);
		lblstatus.setHorizontalAlignment(SwingConstants.CENTER);
		
		setLayout(new BorderLayout());
		add(p1,BorderLayout.CENTER);
		add(p5,BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e){
			System.exit(0);
		}
		});
		
		btnOK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ServerChatConnection chatConn = new ServerChatConnection();
					if(chatConn.connectUser(tfuname.getText(),new String(pfpass.getPassword()))) {
						parent.setVisible(true);
						chatConn.setLoggedInUser(tfuname.getText());
						setVisible(false);
					}
					else {
					lblstatus.setText("Wrong Username/Password!");
					System.out.println("Not Authorized");
					}
			}
		});
		
		btnRegister.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				RegistrationDialog regDialog = new RegistrationDialog();
				regDialog.setVisible(true);	
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				parent.dispose();
				System.exit(0);
				
			}
		});
		
		getRootPane().setDefaultButton(btnOK);
		}
}
