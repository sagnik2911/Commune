package connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.StanzaExtensionFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;

import com.jcraft.jogg.Buffer;

public class ServerChatConnection {
	
	private static AbstractXMPPConnection conn;
	private static ChatManager chatmanager;
	private static JTextPane outputTextPane;
	private static String loggedInUser="";
	private static String chatHistoryFile;
	
	public void setOutputTextArea(JTextPane tp){
		outputTextPane = tp;
	}
		
	public JTextPane getOutputTextPane() {
		return outputTextPane;
	}

	public boolean connectUser(String id,String pass){
		//SmackConfiguration.DEBUG = true;
		XMPPTCPConnectionConfiguration.Builder configBuilder= XMPPTCPConnectionConfiguration.builder();
		configBuilder.setUsernameAndPassword(id, pass);
		configBuilder.setServiceName("Darkdragon");
		configBuilder.setSecurityMode(SecurityMode.disabled);
		
		conn= new XMPPTCPConnection(configBuilder.build());
		
		try {
			conn.connect();
			System.out.println("connected");
			conn.login();
			System.out.println("Logged In !");
			conn.addAsyncStanzaListener(new Red5Plugin(), new StanzaExtensionFilter("redfire-invite", "http://redfire.4ng.net/xmlns/redfire-invite"));

			registerListeners();
			return true;
		} catch (SmackException e){
			System.out.println("Server Not Running");
			JOptionPane.showMessageDialog(null, "Server Not Running", "Commune", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		catch (IOException | XMPPException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public void setMyPresence(String presenceMode) {
		Presence presence=new Presence(Presence.Type.available);
		if(presenceMode.equals("Online"))
			presence.setMode(Presence.Mode.available);
		else if (presenceMode.equals("DND"))
			presence.setMode(Presence.Mode.dnd);
		else
			presence.setMode(Presence.Mode.away);
		try {
			conn.sendStanza(presence);
		} catch (NotConnectedException e) {
			System.out.println("Not Connected");
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Not Connected", "Commune", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void registerListeners(){
		chatmanager = ChatManager.getInstanceFor(conn);
		
		chatmanager.addChatListener(new ChatManagerListener() {
			
			@Override
			public void chatCreated(final Chat chat, final boolean createdLocally) {
				
				if(!createdLocally)
				chat.addMessageListener(new ChatMessageListener() {
					
					@Override
			        public void processMessage(Chat chat, Message message)
			        {
						if(message!=null){
							if(message.getBody()!=null){
								String finaltext = getOutputTextPane().getText().trim().replaceFirst("</body>", makeMsg(chat.getParticipant().split("/")[0],message.getBody()) + "</body>");
								getOutputTextPane().setText(finaltext);
							}
						}
			        }
			      });
			}
		});
	}
	
	public void sendMessage(String to, String msg){
		Chat chat = chatmanager.createChat(to);
			try {
				chat.sendMessage(msg);
			} catch (NotConnectedException e) {
				System.out.println("Msg not delivered. Not Connected");
			}
	}
	
	
	public void setLoggedInUser(String username){
		loggedInUser = username;
	}
	
	public String getLoggedInUser(){
		return loggedInUser;
	}
	
	public String getSessionID(){
		return conn.getStreamId();
	}
	
	public String makeMsg(String user,String text){
		SimpleDateFormat time = new SimpleDateFormat("(hh:mm:ss a)");
		text.replaceAll("[\\u0001-\\u0008\\u000B-\\u001F]", "");
		text = time.format(new java.util.Date())+user+": "+text+"<br>";
	    String html_txt = "<div style='background-color: #349e92;border: 1px dotted black;'><b style='color: white;'>" + text + "</b></div>";
	    return html_txt;
	}
	
	public void searchUser(String name){
		UserSearchManager searchMgr = new UserSearchManager(conn);
		try {
			Form searchFrom = searchMgr.getSearchForm("search."+conn.getServiceName());
			Form answerForm = searchFrom.createAnswerForm();
			answerForm.setAnswer("Name", true);
			answerForm.setAnswer("search", name);
			
			ReportedData resultData = searchMgr.getSearchResults(answerForm, "search."+conn.getServiceName());
			if(resultData!=null){
				List<Row> rows = resultData.getRows();
				Iterator<Row> it = rows.iterator();
				if(it.hasNext()){
					Row row = it.next();
					/*List<String> values = row.getValues("jid");
					Iterator<String> iterator = values.iterator();
					if(iterator.hasNext()){*/
						//String value = iterator.next();
						String result = "JID : "+row.getValues("jid").iterator().next().toString()+"\n\n"
								+ "Username : "+row.getValues("Username").iterator().next().toString()+"\n\n"
										+ "Name : "+row.getValues("Name").iterator().next().toString();
						JOptionPane.showMessageDialog(null, result, "User Search Results :", JOptionPane.INFORMATION_MESSAGE);
					//}
				}
				else
					JOptionPane.showMessageDialog(null, "No Such User", "User Search Results: ", JOptionPane.WARNING_MESSAGE);
			}
			
		} catch (NoResponseException | XMPPErrorException | NotConnectedException e) {
			System.out.println("Error in search");
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		conn.disconnect();
	}

	public RosterManager getRosterManager() {
		RosterManager rm = new RosterManager(conn);
		return rm;
	}
	
	public static AbstractXMPPConnection getConn(){
		if(conn!= null)
			return conn;
		else
			return null;
	}

	public void saveChat() {
		String userPath = System.getProperty("user.home");
		System.out.println("Logs saved in :"+userPath);
		String userFolder = userPath+File.separator+"Commune"+File.separator+getLoggedInUser();
		File theDir = new File(userFolder);
		
		boolean folderCreate = true;
		if(!theDir.exists()){
			folderCreate = theDir.mkdirs();
		}
		if(folderCreate){
			try {
				chatHistoryFile = userFolder+File.separator+"theSuperSecretChatFile.blahblah";
				FileWriter fw = new FileWriter(chatHistoryFile); // Last day before submission. I really like this filename :P
				fw.append(getOutputTextPane().getText());
				fw.close();
			} catch (IOException e) {
				System.out.println("Error writing log file");
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Couldn't create chat directory!");
		}
	}

	public void getChatHistory() {
		String userPath = System.getProperty("user.home");
		String userFolder = userPath+File.separator+"Commune"+File.separator+getLoggedInUser();
		chatHistoryFile = userFolder+File.separator+"theSuperSecretChatFile.blahblah";

		try{
			FileReader fr = new FileReader(chatHistoryFile);
			
			BufferedReader br = new BufferedReader(fr);
			getOutputTextPane().read(br, chatHistoryFile);
		}
		catch(FileNotFoundException e){
			//ignore, probably first chat so no history
		}
		catch(Exception e){
			System.out.println("Exception in getting chat history");
			e.printStackTrace();
		}
	}
}
