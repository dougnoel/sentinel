package sentinel.apis;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;

import sentinel.utils.AuthenticationType;
import sentinel.utils.WebDriverFactory;

public abstract class API {
	private static final Logger log = LogManager.getLogger(API.class.getName()); // Create a logger.
	
    static protected final AuthenticationType JWT = AuthenticationType.JWT;
    static protected final AuthenticationType AUTH_KEY = AuthenticationType.AUTH_KEY;
    static protected final AuthenticationType NONE = AuthenticationType.NONE;
    
	protected AuthenticationType authenticationType = NONE;
	Object authToken = null;
	
	protected URL url = null;
	
	public API() {
	}

    public String getName() {
        return this.getClass().getSimpleName();
    }
    
	public void setURL(String url) throws MalformedURLException {
		this.url = new URL(url);
	}
	
	public void setURL(URL url) {
		this.url = url;
	}
	
	public URL getURL() {
		return url;
	}
	
	public URI getURI() {
		URI uri = null;
		try {
			uri = url.toURI();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uri;
	}
	
	public void setAuthType(AuthenticationType authType) {
		if(authType == JWT || authType == AUTH_KEY) {
			authenticationType = authType;
		}
	}
	
	public String getAuthToken() {
		String token = null;
		if (authToken != null) {
			token = authToken.toString();
		}
		return token;
	}
	
	public void setAuthToken() {
		switch (authenticationType) {
		case JWT:
			String jsExpression = "return JSON.parse(window.sessionStorage[Object.keys(window.sessionStorage).filter(key => /^oidc.*$/.test(key)).shift()]).id_token";
			authToken = ((JavascriptExecutor) WebDriverFactory.getWebDriverAndHandleErrors()).executeScript(jsExpression);
			break;
		case AUTH_KEY:
			//TODO: Implement Auth Token setting
			log.error("Auth Token capture is not yet implemented.");
		case NONE:
			break;
		}
	}
}
