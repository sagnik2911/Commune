package connection;

import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.packet.RosterPacket;


public class RosterManager {
 
        private  Roster rs = null;
        private  AbstractXMPPConnection xmppconn;
        private Presence presence;
        //Collection<RosterEntry> entries;
        
        
	public RosterManager (AbstractXMPPConnection xmppconn) {
        
        try{
            this.xmppconn = xmppconn;
            rs = Roster.getInstanceFor(xmppconn);
            if (!rs.isLoaded()) 
                rs.reloadAndWait();
            this.presence = new Presence(Presence.Type.available);
            setStatus("Hi There!");  //setting status at each startup
            setPresence(Mode.available);
            rs.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
            presence.setPriority(24);
            
            this.xmppconn.sendStanza(presence);
            
        }
        catch (NotConnectedException | NotLoggedInException | InterruptedException e) {
            
            JOptionPane.showMessageDialog(null,"There was a problem", "Commune", JOptionPane.ERROR_MESSAGE);
        }   
    }
    
        public Roster getRoster(){
            // returns the Roster for the connection
            return rs;
        }
    
    public String getMode(String jid){
         //returns the mode for given jid
        String mode = null;
        try{
            mode = GetPresence(jid).getMode().toString();
        }
        catch(Exception ex){
            mode = "available";
        }
        return mode;
    }
    
    public boolean addFriend(String name, String nickname){
        /*
         * Pass the name and nickname for the user you
         * want to add. Use full jid like sam@server_name.
         * return true if done else false.
         * 
         */
        
        try{
            if(!rs.contains(name)){  //If not present in friend list
                rs.createEntry(name, nickname, null);
                //xmppconn.sendPacket(new Presence(Presence.Type.available, "Changed", 128, Mode.available));
                return true;
            }
            return false;
        }
        catch(XMPPException | NotConnectedException | NotLoggedInException | NoResponseException e){
            JOptionPane.showMessageDialog(null, "Not Connected/Logged In", "Commune", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
    }
    
    public String getStatus(String jid){
        //get status of user for given jid
        Presence jidPresence = GetPresence(jid+"@"+xmppconn.getServiceName());
        return jidPresence.getStatus();
    }
    
	public void deleteFriend(String user){
        /* delete friend for given jid
         * use full jid sam@server
         */
        RosterEntry entry = rs.getEntry(user);
        //sending packet to delete specified user
        RosterPacket rp = new RosterPacket();
        rp.setType(IQ.Type.set);
        RosterPacket.Item item = new RosterPacket.Item(entry.getUser(), entry.getName());
        item.setItemType(RosterPacket.ItemType.remove);
        rp.addRosterItem(item);
        try {
			xmppconn.sendStanza(rp);
		} catch (NotConnectedException e) {
			JOptionPane.showMessageDialog(null, "Not Connected", "Commune", JOptionPane.ERROR_MESSAGE);
		}
    }
    
    public List<String> getUserOnline(){
        List<String> usersOnline = new Vector<String>();
        for (RosterEntry entry : rs.getEntries())
        {       
            System.out.println("Presence: " +rs.getPresence(entry.getUser()));
            Presence thepresence = rs.getPresence(entry.getUser()); 
            System.out.println("Taken from user: "+entry.getUser());
            System.out.println("entries: "+rs.getEntries().toString());
            //:TODO xmppconn.sendPacket(presence);
            if(thepresence.isAvailable()){
                    String jid = entry.getUser();
                    String username = jid.substring(0, jid.indexOf("@"));
                    String mode = getMode(jid);
                    usersOnline.add(username+" - "+getStatus(jid)+" - "+mode);                   
            }
        }
        return usersOnline;
    }
    
    public Vector<String> getUserOffline(){
        //Metodo per estrapolazione degli utenti offline amici del richiedente
        Vector<String> usersOffline = new Vector<String>();
        for (RosterEntry entry : rs.getEntries())
        {       
                Presence thepresence = rs.getPresence(entry.getUser()/*+"@"+"darkdragon*/+"/Smack");
                if(!thepresence.isAvailable())
                    usersOffline.add(entry.getName());
        }
        return usersOffline;
    }
    
    public Presence GetPresence(String jid){
        //Metodo simile a getMod solo che rilascia un 
        //oggetto Presence al contrario di getMod che rilascia stinga
        /* get the presence of the select user with the jid's user.
         * the jid format is user@domain/resource
         */
        Presence pres = rs.getPresence(jid);
        return pres;
        
    }
    
	public void setStatus(String text){
        /* set the precence at the select jid's user
         * the jid format is user@domain/resource
         */
        this.presence.setStatus(text);
        try {
			this.xmppconn.sendStanza(presence);
		} catch (NotConnectedException e) {
			JOptionPane.showMessageDialog(null, "Not Connected", "Commune", JOptionPane.ERROR_MESSAGE);
		}
    }
    
	public void setPresence(Mode mode){
        //sets presence for give mode
        this.presence.setMode(mode);
        //this.presence.setType(Presence.Type.available);
        //this.presence.setMode(Presence.Mode.available);
        try {
			this.xmppconn.sendStanza(presence);
		} catch (NotConnectedException e) {
			JOptionPane.showMessageDialog(null, "Not Connected", "Commune", JOptionPane.ERROR_MESSAGE);
		}
    }

}