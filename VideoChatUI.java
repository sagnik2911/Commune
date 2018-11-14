package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;


public class VideoChatUI {

	JFrame frame;
	private static Webcam webcam;
	JPanel ownCamPanel;
	WebcamPanel webcamPanel;
	private String to;
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VideoChatUI window = new VideoChatUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					webcam.close();
					e.printStackTrace();
				}
			}
		});
	} commented as it will be called from MainWindow. 
	  uncomment this to test it separately*/

	/**
	 * Create the application.
	 */
	public VideoChatUI(String to) {
		this.to = to;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame=new JFrame("Video Chat with : "+to);
		frame.setBounds(350,150,715,450);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		webcam = Webcam.getDefault();
		webcam.setViewSize(new Dimension(176, 144));
		webcam.open();
		webcamPanel = new WebcamPanel(webcam,false);
		
		ownCamPanel = new JPanel();
		ownCamPanel.setBackground(Color.BLACK);
		ownCamPanel.setBounds(515, 279, 174, 121);
		
		frame.getContentPane().add(ownCamPanel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setBounds(10, 11, 494, 289);
		frame.getContentPane().add(panel_1);
				
		final JButton btnCam = new JButton("CAM : OFF");
		btnCam.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnCam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				if(a.getActionCommand().equals("CAM : OFF")){
					btnCam.setText("CAM : ON");
					btnCam.setActionCommand("CAM : ON");
					changeCamStatus(true);
				}
				else{
					btnCam.setText("CAM : OFF");
					btnCam.setActionCommand("CAM : OFF");
					changeCamStatus(false);
				}
			}
		});
		btnCam.setBounds(553, 257, 110, 23);
		frame.getContentPane().add(btnCam);
		
		JTextArea chatArea = new JTextArea();
		chatArea.setBackground(new Color(192, 192, 192));
		chatArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		chatArea.setBounds(10, 352, 431, 37);
		frame.getContentPane().add(chatArea);
		
		JButton btnSend = new JButton("SEND");
		btnSend.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnSend.setBounds(451, 358, 60, 23);
		frame.getContentPane().add(btnSend);
		
	}
	
	private void changeCamStatus(boolean state){
		if(state){
			webcamPanel.start();
			webcamPanel.setFPSDisplayed(true);
			//webcamPanel.setImageSizeDisplayed(true);
			webcamPanel.setMirrored(true);
			
			ownCamPanel.add(webcamPanel);
			ownCamPanel.getParent().revalidate();
		}
		else{
			webcamPanel.stop();
		}
		
	}
}
