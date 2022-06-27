package com.dougnoel.sentinel.apis;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;

import com.dougnoel.sentinel.enums.AuthenticationType;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

public abstract class API {
	private static final Logger log = LogManager.getLogger(API.class.getName()); // Create a logger.
	
	protected static final AuthenticationType JWT = AuthenticationType.JWT;
	protected static final AuthenticationType AUTH_KEY = AuthenticationType.AUTH_KEY;
	protected static final AuthenticationType NONE = AuthenticationType.NONE;
    
	protected AuthenticationType authenticationType = NONE;
	Object authToken = null;
	
	protected URL url = null;
	
	protected API() {
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
			log.error(e.getReason());
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
	
	/**
	 * Creates an AuthToken
	 */
	public void setAuthToken() {
		switch (authenticationType) {
		case JWT:
			String jsExpression = "return JSON.parse(window.sessionStorage[Object.keys(window.sessionStorage).filter(key => /^oidc.*$/.test(key)).shift()]).id_token";
			authToken = ((JavascriptExecutor) WebDriverFactory.getWebDriver()).executeScript(jsExpression);
			break;
		case AUTH_KEY:
			//TODO: Implement Auth Token setting
			log.error("Auth Token capture is not yet implemented.");
			break;
		case NONE:
			break;
		}
	}
}
