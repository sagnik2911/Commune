package connection;

import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;

import org.jdesktop.jdic.browser.BrowserEngineManager;
import org.jdesktop.jdic.browser.WebBrowser;
import org.jdesktop.jdic.browser.WebBrowserEvent;
import org.jdesktop.jdic.browser.WebBrowserListener;
import org.jivesoftware.spark.component.browser.BrowserViewer;

public class NativeBrowserViewer extends BrowserViewer  implements WebBrowserListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WebBrowser browser;
    private String url;
    
	@Override
	public void documentCompleted(WebBrowserEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void downloadCompleted(WebBrowserEvent arg0) {
		 if(browser == null || browser.getURL() == null)
	        {
	            return;

	        } else {

	            String url = browser.getURL().toExternalForm();
	            documentLoaded(url);
	            return;
	        }
		
	}

	@Override
	public void downloadError(WebBrowserEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void downloadProgress(WebBrowserEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void downloadStarted(WebBrowserEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializationCompleted(WebBrowserEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void statusTextChange(WebBrowserEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void titleChange(WebBrowserEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goBack() {
		browser.back();
		
	}

	@Override
	public void initializeBrowser() {
		browser = new WebBrowser();
        setLayout(new BorderLayout());
        add(browser, "Center");
        browser.addWebBrowserListener(this);
		
	}

	@Override
	public void loadURL(String url) {
		this.url = url;

        try
        {
            browser.setURL(new URL(url));
        }
        catch(MalformedURLException e)
        {
            System.out.println(e);
        }
		
	}
	
	  public void reloadURL()
	    {
	        try
	        {
	            browser.setURL(new URL(url));
	        }
	        catch(MalformedURLException e)
	        {
	            System.out.println(e);
	        }
		}

}
