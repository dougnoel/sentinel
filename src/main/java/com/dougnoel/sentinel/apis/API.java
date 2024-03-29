package com.dougnoel.sentinel.apis;

import java.net.URISyntaxException;
import java.util.List;

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
	protected URIBuilder getURIBuilder(String passedText) throws URISyntaxException {
		String swaggerUrl = Configuration.getURL(APIManager.getAPI());
		SwaggerParseResult result = new OpenAPIParser().readLocation(swaggerUrl, null, null);
		OpenAPI openAPI = result.getOpenAPI();
		List<Server> servers = openAPI.getServers();

		try {
			var firstServer = servers.get(0).getUrl();
			var uriBuilder = new URIBuilder(firstServer + passedText);
			if(!uriBuilder.isAbsolute()) {
				uriBuilder = (new URIBuilder(swaggerUrl)).setPath(passedText);
			}
			return uriBuilder;
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
	}
	
	public Request getRequest() {
		return request; 
	}

}
