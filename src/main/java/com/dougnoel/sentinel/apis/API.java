package com.dougnoel.sentinel.apis;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.URIBuilder;
import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.enums.YAMLObjectType;
import com.dougnoel.sentinel.exceptions.IOException;
import com.dougnoel.sentinel.system.YAMLObject;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

public class API extends YAMLObject {
	private Request request = new Request();
	protected Map<String,Response> responses = new HashMap<>();
	
    /**
     * Constructor
     * @param apiName String the exact case-sensitive name of the yaml file containing the API information.
     */
    public API(String apiName) {
    	super(apiName);
    	this.yamlObjectType = YAMLObjectType.API;
    }
		
	/**
	 * Returns a java.net.URI constructed from the URL listed in the API yaml file.
	 * 
	 * @return java.net.URI the constructed URI
	 */
	protected URIBuilder getURIBuilder(String passedText) {		
		String swaggerUrl = Configuration.getURL(APIManager.getAPI());
		SwaggerParseResult result = new OpenAPIParser().readLocation(swaggerUrl, null, null);
		OpenAPI openAPI = result.getOpenAPI();
		List<Server> servers = openAPI.getServers();
		
		try {
			return new URIBuilder(servers.get(0).getUrl() + passedText);
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * Returns the currently constructed request for this API.
	 * 
	 * @return com.dougnoel.sentinel.apis.Request the currently constructed request.
	 */
	public Request getRequest() {
		return request; 
	}
	
	/**
	 * Stores a Response based on the key passed.
	 * 
	 * @param key String the key to store the Response under
	 * @param com.dougnoel.sentinel.apis.Response the Response object to store
	 */
	public void setResponse(String key, Response response) {
		responses.put(key, response);
	}
	
	/**
	 * Returns the stored response for the given key.
	 * 
	 * @param key String the key the Response is stored under
	 * @return com.dougnoel.sentinel.apis.Response the Response object requested
	 */
	public Response getResponse(String key) {
		return responses.get(key);
	}

}
