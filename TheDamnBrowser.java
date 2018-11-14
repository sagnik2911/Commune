package connection;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Message;

/**
 * @author Sagnik Ghosh
 * Okay Lets be honest here. This is a fallback class.
 * It will open your default browser.
 * I will delete this as soon as I can get an Embedded Browser to work.
 */
public class TheDamnBrowser implements ActionListener {
	ServerChatConnection chatConn;
	private String protocol = "http://";
	private String red5server = "localhost";
	private String red5port = "7070";
	private String url;
	
	private String sessionID;
	private String newURL;
	@Override
	public void actionPerformed(ActionEvent ae) {
		chatConn = new ServerChatConnection();
		
		int width = 680;
		int height = 520;
		
		sessionID = chatConn.getSessionID();
		String me = chatConn.getLoggedInUser();
		String youJID = JOptionPane.showInputDialog("Enter full jid of Receipent : ");
		if ((youJID != null) && !(youJID.isEmpty())) {
			
			//taking server IP from user as there is no dns resolver
			String server_name = JOptionPane.showInputDialog("Enter Server IP/Name :");
			if ((server_name != null) && !(server_name.isEmpty()))
				red5server = server_name;
			
			url = protocol + red5server + ":" + red5port; 
			newURL = url + "/redfire/video/redfire_2way.html?me="+me+"&you="+youJID.split("@")[0]+"&key="+sessionID;
			String youURL = url + "/redfire/video/redfire_2way.html?you="+me+"&me="+youJID.split("@")[0]+"&key="+sessionID;
			
			if(Desktop.isDesktopSupported()){
				try {
					Desktop.getDesktop().browse(new URI(newURL));
				} catch (IOException | URISyntaxException e1) {
					System.out.println("Exception in red5 connection");
					e1.printStackTrace();
				}
			}
			sendInvite(" is offering to share audio and video in this chat", youJID, youURL, width, height);
			System.out.println("myURL: "+newURL);
			System.out.println("youURL: "+youURL);
			
		} else {
			JOptionPane.showMessageDialog(null, "Name cannot be empty!", "No Receipent",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void sendInvite(String prompt, String jid, String url, int width, int height)
	{
		Message message2 = new Message();
		message2.setTo(jid);
		message2.setType(Message.Type.chat);
		message2.setBody(url);
		RedfireExtension redfireExtension = new RedfireExtension();
		redfireExtension.setValue("sessionID", sessionID);
		redfireExtension.setValue("width",  String.valueOf(width));
		redfireExtension.setValue("height", String.valueOf(height));
		redfireExtension.setValue("nickname", chatConn.getLoggedInUser());
		redfireExtension.setValue("prompt", prompt);
		message2.addExtension(redfireExtension);
		try {
			ServerChatConnection.getConn().sendStanza(message2);
		} catch (NotConnectedException e) {
            JOptionPane.showMessageDialog(null,"Not Connected", "Commune", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
