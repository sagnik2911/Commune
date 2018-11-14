package test;

import java.util.List;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

public class TestOfflineMsg {
	
	private AbstractXMPPConnection conn;
	
	void loginAndRetrieve(){
		XMPPTCPConnectionConfiguration.Builder configBuilder= XMPPTCPConnectionConfiguration.builder();
		configBuilder.setServiceName("DarkDragon");
		configBuilder.setUsernameAndPassword("sam", "12345");
		//setting presence to false
		configBuilder.setSendPresence(false);
		configBuilder.setSecurityMode(SecurityMode.disabled);
		
		conn= new XMPPTCPConnection(configBuilder.build());
		try{
			conn.connect();
			OfflineMessageManager offMgr = new OfflineMessageManager(conn);
			
			conn.login();
			
			if(offMgr.supportsFlexibleRetrieval()){
				List<Message> msgs;
				msgs = offMgr.getMessages();
				for(Message m : msgs){
					System.out.println(m.getFrom()+"---"+m.getBody());
				}
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		TestOfflineMsg ob = new TestOfflineMsg();
		ob.loginAndRetrieve();
	}

}
