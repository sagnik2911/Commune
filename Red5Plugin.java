package connection;

import org.jivesoftware.smack.SmackException.NotConnectedException;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

public class Red5Plugin implements StanzaListener{
		
	public void processPacket(Stanza stanza) throws NotConnectedException {
		try {
			ExtensionElement redfireExtension = null;

			if (stanza instanceof Message)
			{
				Message message = (Message) stanza;
				
				redfireExtension = message.getExtension("redfire-invite", "http://redfire.4ng.net/xmlns/redfire-invite");

				if (redfireExtension != null && "error".equals(message.getType().toString()) == false)
				{
					StringBuilder justTemp = new StringBuilder(redfireExtension.toXML());
					justTemp.append(redfireExtension.toXML());
					String xml = justTemp.toString();

					String nickname = getTag(xml, "nickname");
					String url = message.getBody();
					String prompt = getTag(xml, "prompt");

					int width = Integer.parseInt(getTag(xml, "width"));
					int height = Integer.parseInt(getTag(xml, "height"));

					//Log.warning("RedfireExtension  invite received " + roomId);

					showBrowser(width, height, nickname, prompt, url);

				}
			}
		}
		catch (Exception e) {

			System.out.println("Error process packet:"+e);
			e.printStackTrace();
		}

	}
	
	private String getTag(String xml, String tag) {
		String tagValue = null;

		int pos = xml.indexOf("<" + tag + ">");

		if (pos > -1) {
			String temp = xml.substring(pos + tag.length() + 2);
			pos = temp.indexOf("</" + tag + ">");

			if (pos > -1)
			{
				tagValue = temp.substring(0, pos);
			}
		}

		return (tagValue);
	}
	
	private void showBrowser(final int width, final int height, final String nickname, final String prompt, final String url)
    {
		//BareBonesBrowserLaunch.openURL(width, height, url, "Commune"); :TODO using the native browser now
		if(Desktop.isDesktopSupported()){
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (IOException | URISyntaxException e1) {
				System.out.println("Exception in red5 connection");
				e1.printStackTrace();
			}
		}
    }

}
