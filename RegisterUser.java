package connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

public class RegisterUser {
	private AbstractXMPPConnection conn;
	private String user,pass,server,name;
	public RegisterUser(String user, String pass, String server, String name){
		SmackConfiguration.DEBUG = true;
		//System.setProperty("smack.debugEnabled","true");
		this.user = user;
		this.pass = pass;
		this.server = server;
		this.name = name;
		XMPPTCPConnectionConfiguration.Builder conf= XMPPTCPConnectionConfiguration.builder();
		conf.setServiceName(server);
		conf.setSecurityMode(SecurityMode.disabled);
		
		conn= new XMPPTCPConnection(conf.build());
	}
	
	public boolean register() {
		try {
			conn.connect();
			AccountManager am = AccountManager.getInstance(conn);
			System.out.println(user+" "+pass);
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("Jid", user+"@"+server);
			attributes.put("Name", name);
			am.createAccount(user, pass, attributes);
			return true;
		} catch (SmackException | IOException | XMPPException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			conn.disconnect();
		}
		
	}
}
