package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import connection.RegisterUser;

public class RegistrationDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private final JPanel introPanel = new JPanel();
	private JTextField username;
	private JPasswordField password;
	private JTextField server;
	private JButton okButton;
	private JButton cancelButton;
	private JLabel labelStatus;
	private JTextField name;
	private JLabel lblName;

	/**
	 * Launch the application.
	 
	public static void main(String[] args) {
		try {
			RegistrationDialog dialog = new RegistrationDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 */
	/**
	 * Create the dialog.
	 */
	public RegistrationDialog() {		
		setTitle("Create New Account");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		setBounds(100, 100, 461, 344);
		getContentPane().setLayout(new BorderLayout());
		introPanel.setBackground(Color.WHITE);
		getContentPane().add(introPanel, BorderLayout.NORTH);
		{
			JLabel lblNewLabel = new JLabel("Account Registration");
			introPanel.add(lblNewLabel);
		}
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblUsername = new JLabel("Username :");
			lblUsername.setBounds(34, 37, 77, 22);
			contentPanel.add(lblUsername);
		}
		
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setBounds(34, 70, 77, 22);
		contentPanel.add(lblPassword);
		
		JLabel lblServer = new JLabel("Server :");
		lblServer.setBounds(34, 105, 77, 22);
		contentPanel.add(lblServer);
		
		username = new JTextField();
		username.setName("Username");
		username.setBounds(134, 38, 267, 20);
		contentPanel.add(username);
		username.setColumns(10);
		
		password = new JPasswordField();
		password.setName("Password");
		password.setColumns(10);
		password.setBounds(134, 71, 267, 20);
		contentPanel.add(password);
		
		server = new JTextField();
		server.setName("Server");
		server.setColumns(10);
		server.setBounds(134, 106, 267, 20);
		
		name = new JTextField();
		name.setBounds(134, 142, 267, 20);
		contentPanel.add(name);
		name.setColumns(10);
		
		final JTextField[] arraytf = new JTextField[4];
		arraytf[0] = username;
		arraytf[1] = password;
		arraytf[2] = server;
		arraytf[3] = name;
		
		contentPanel.add(server);
		
		labelStatus = new JLabel("");
		labelStatus.setForeground(Color.RED);
		labelStatus.setBounds(63, 194, 338, 14);
		contentPanel.add(labelStatus);
		
				
		lblName = new JLabel("Name : ");
		lblName.setBounds(34, 148, 46, 14);
		contentPanel.add(lblName);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Create Account");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean flag = true;
				for(JTextField tf : arraytf){
					if(tf.getText().trim().length()== 0){
						JOptionPane.showMessageDialog(null, tf.getName()+" is empty!");
						tf.requestFocusInWindow();
						flag = false;
						break;
					}
				}
				if(flag){
					RegisterUser reg = new RegisterUser(username.getText(), new String(password.getPassword()), server.getText(), name.getText());
					boolean status = reg.register();
					if(status){
						labelStatus.setText("Registration Done Successfully..Close This Window");
					}
					else {
						labelStatus.setText("Registration not done. Check Log");
					}
				}
				
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
	}
}
