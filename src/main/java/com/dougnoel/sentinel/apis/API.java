package com.dougnoel.sentinel.apis;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;

import com.dougnoel.sentinel.apis.actions.Action;
import com.dougnoel.sentinel.apis.actions.ActionFactory;
import com.dougnoel.sentinel.enums.AuthenticationType;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

public class API {
	private static final Logger log = LogManager.getLogger(API.class.getName()); // Create a logger.
	private String apiName;
	
	protected static final AuthenticationType JWT = AuthenticationType.JWT;
	protected static final AuthenticationType AUTH_KEY = AuthenticationType.AUTH_KEY;
	protected static final AuthenticationType NONE = AuthenticationType.NONE;
    
	protected AuthenticationType authenticationType = NONE;
	Object authToken = null;
	
	protected Map<String,Action> actions;
	
	protected URL url = null;
	
    /**
     * Constructor
     * @param apiName String the exact case-sensitive name of the yaml file containing the API information.
     */
    public API(String apiName) {
    	this.apiName = apiName;
        actions = new HashMap<>();
    }

    /**
     * Returns the name of this API as a string.
     * 
     * @return String apiName
     */
    public String getName() {
        return apiName;
    }
    
	public Action getAction(String actionName) {
        String normalizedName = actionName.replaceAll("\\s+", "_").toLowerCase();
        return actions.computeIfAbsent(normalizedName, name -> ((Action)(ActionFactory.createAction(name, this))));
    }
    
	public void setURL(String url) throws MalformedURLException {
		this.url = new URL(url);
	}
	
	public void setURL(URL url) {
		this.url = url;
	}
	
	/**
	 * Returns a java.net.URI constructed from the URL listed in the API yaml file.
	 * 
	 * @return java.net.URI the constructed URI
	 */
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
