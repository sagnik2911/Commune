package connection;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jivesoftware.spark.component.browser.BrowserViewer;
import org.jivesoftware.spark.component.browser.EmbeddedBrowserViewer;

public class BareBonesBrowserLaunch {
	static final String[] browsers = { "google-chrome", "firefox", "opera","epiphany", "konqueror", "conkeror", "midori", "kazehakase", "mozilla" };
	   static final String errMsg = "Error attempting to launch web browser";

	   static final Map<String, JFrame> windows = new HashMap<String, JFrame>();
	   static final Map<String, BrowserViewer> viewers = new HashMap<String, BrowserViewer>();


	   /**
	    * Opens the specified web page in the user's default browser
	    * @param url A web address (URL) of a web page (ex: "http://www.google.com/")
	    */

	   public static void closeURL(String title)
	   {
		   	if (windows.containsKey(title))
		   	{
				JFrame frame = windows.get(title);
				frame.dispose();
				windows.remove(title);

				BrowserViewer viewer = viewers.get(title);
				viewers.remove(title);

			}
	   }

	   public static void openURL(int width, int height, String url, String title)
	   {
	      try {

			String OS = System.getProperty("os.name").toLowerCase();

		 	System.out.println("BareBonesBrowserLaunch.openURL: " + url + " " + title + " " + OS);

			if (OS.indexOf("windows") > -1)
			{
				openBrowserURL(width, height, url, title);

			} else {

			  	//attempt to use Desktop library from JDK 1.6+

	         	Class<?> d = Class.forName("java.awt.Desktop");
	         	d.getDeclaredMethod("browse", new Class[] {java.net.URI.class}).invoke(d.getDeclaredMethod("getDesktop").invoke(null), new Object[] {java.net.URI.create(url)});

	         	//above code mimicks:  java.awt.Desktop.getDesktop().browse()
		 	}

	     } catch (Exception ignore) {  //library not available or failed

	    	 System.out.println("BareBonesBrowserLaunch.openURL: ignore " + ignore);

	         String osName = System.getProperty("os.name");

	         try {

	            if (osName.startsWith("Mac OS"))
	            {
	               Class.forName("com.apple.eio.FileManager").getDeclaredMethod("openURL", new Class[] {String.class}).invoke(null, new Object[] {url});

	            }  else if (osName.startsWith("Windows")) {
	               Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);


	            } else { //assume Unix or Linux
	               String browser = null;

	               for (String b : browsers)
	               {
	                  if (browser == null && Runtime.getRuntime().exec(new String[] {"which", b}).getInputStream().read() != -1)
	                  {
	                     Runtime.getRuntime().exec(new String[] {browser = b, url});
					  }
				   }

	               if (browser == null)
	               {
	                  throw new Exception(Arrays.toString(browsers));
	               }
	            }

	         } catch (Exception e) {
	        	 System.out.println("BareBonesBrowserLaunch.openURL: error " + e);
	            JOptionPane.showMessageDialog(null, errMsg + "\n" + e.toString());
	         }
	      }
		}

	   private static void openBrowserURL(int width, int height, String url, String title)
	   {
		   	if (windows.containsKey(title))
		   	{
				JFrame frame = windows.get(title);
				BrowserViewer viewer = viewers.get(title);

				frame.setSize(width, height);
				viewer.loadURL(url);

			} else {

				BrowserViewer viewer = new EmbeddedBrowserViewer(); //TODO: fuck this thing
				viewer.initializeBrowser();

				JFrame frame = new JFrame(title);

				frame.addWindowListener(new java.awt.event.WindowAdapter() {

					public void windowClosing(WindowEvent winEvt)
					{
						JFrame frame = (JFrame) winEvt.getWindow();
						String title = frame.getTitle();

						frame.dispose();
						windows.remove(title);

						BrowserViewer viewer = viewers.get(title);
						viewers.remove(title);
					}
				});

			
				frame.getContentPane().setLayout(new BorderLayout());
				frame.getContentPane().add(viewer, BorderLayout.CENTER);
				frame.setVisible(true);
				frame.pack();
				frame.setSize(width, height);
				viewer.loadURL(url);

				windows.put(title, frame);
				viewers.put(title, viewer);
			}
	   }

	}

