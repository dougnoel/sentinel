package com.dougnoel.sentinel.apis;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.enums.AuthenticationType;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

public class API {
	private static final Logger log = LogManager.getLogger(API.class.getName()); // Create a logger.
	private String apiName;
	
	protected static final AuthenticationType JWT = AuthenticationType.JWT;
	protected static final AuthenticationType AUTH_KEY = AuthenticationType.AUTH_KEY;
	protected static final AuthenticationType NONE = AuthenticationType.NONE;
    
	protected AuthenticationType authenticationType = NONE;
	Object authToken = null;
	
	protected URL url = null;
	
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
	 * Returns a java.net.URI constructed from the URL listed in the API yaml file.
	 * 
	 * @return java.net.URI the constructed URI
	 */
	public URIBuilder getURIBuilder(String passedText) {		
		String swaggerUrl = Configuration.getAPIURL(APIManager.getAPI().getName());
		SwaggerParseResult result = new OpenAPIParser().readLocation(swaggerUrl, null, null);
		OpenAPI openAPI = result.getOpenAPI();
		List<Server> servers = openAPI.getServers();
		
		try {
			return new URIBuilder(servers.get(0).getUrl() + passedText);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
