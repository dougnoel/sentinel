package com.dougnoel.sentinel.apis;

import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.enums.AuthenticationType;
import com.dougnoel.sentinel.exceptions.IOException;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

public class API {
	private static final Logger log = LogManager.getLogger(API.class.getName()); // Create a logger.
	private String apiName;
	private AuthenticationType authenticationType = NONE;
	private Object authToken = null;
	private Request request = new Request();
	
	private static final AuthenticationType JWT = AuthenticationType.JWT;
	private static final AuthenticationType AUTH_KEY = AuthenticationType.AUTH_KEY;
	private static final AuthenticationType NONE = AuthenticationType.NONE;
	
    /**
     * Constructor
     * @param apiName String the exact case-sensitive name of the yaml file containing the API information.
     */
    public API(String apiName) {
    	this.apiName = apiName;
    }

    /**
     * Returns the name of this API as a string.
     * 
     * @return String apiName
     */
    public String getName() {
        return apiName;
    }
	
	/**
	 * Sets the authentication type that this API uses.
	 * @param authType com.dougnoel.sentinel.enums.AuthenticationType the type of authentication this API uses
	 */
	public void setAuthType(AuthenticationType authType) {
		if(authType == JWT || authType == AUTH_KEY) {
			authenticationType = authType;
		}
	}
	
	/**
	 * Returns the authentication token.
	 * @return String the authentication token
	 */
	public String getAuthToken() {
		String token = null;
		if (authToken != null) {
			token = authToken.toString();
		}
		return token;
	}
	
	/**
	 * Creates an authentication token based on the authentication type set. By capturing it from the currently open browser window.
	 */
	public void setAuthToken() {
		switch (authenticationType) {
		case JWT:
			String jsExpression = "return JSON.parse(window.sessionStorage[Object.keys(window.sessionStorage).filter(key => /^oidc.*$/.test(key)).shift()]).id_token";
			//return window.localStorage.getItem('id_token')").ToString()
			authToken = ((JavascriptExecutor) WebDriverFactory.getWebDriver()).executeScript(jsExpression);
			break;
		case AUTH_KEY:
			log.error("Auth Token capture is not yet implemented.");
			break;
		case NONE:
			break;
		}
	}
		
	/**
	 * Returns a java.net.URI constructed from the URL listed in the API yaml file.
	 * 
	 * @return java.net.URI the constructed URI
	 */
	protected URIBuilder getURIBuilder(String passedText) {		
		String swaggerUrl = Configuration.getAPIURL(APIManager.getAPI().getName());
		SwaggerParseResult result = new OpenAPIParser().readLocation(swaggerUrl, null, null);
		OpenAPI openAPI = result.getOpenAPI();
		List<Server> servers = openAPI.getServers();
		
		try {
			return new URIBuilder(servers.get(0).getUrl() + passedText);
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
	}
	
	public Request getRequest() {
		return request; 
	}

}
