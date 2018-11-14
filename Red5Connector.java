package connection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Message;

public class Red5Connector implements ActionListener {

	ServerChatConnection chatConn;
	private String protocol = "http://";
	private String red5server = "localhost";
	private String red5port = "7070";
	private String url = protocol + red5server + ":" + red5port; 
	
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
			newURL = url + "/redfire/video/redfire_2way.html?me="+me+"&you="+youJID.split("@")[0]+"&key="+sessionID;
			String youURL = url + "/redfire/video/redfire_2way.html?you="+me+"&me="+youJID.split("@")[0]+"&key="+sessionID;
			
			BareBonesBrowserLaunch.openURL(width, height, newURL, "Commune");
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
