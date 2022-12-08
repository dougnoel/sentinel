package com.dougnoel.sentinel.apis;

import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;
import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.IOException;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

public class API {
	private String apiName;
	private Request request = new Request();
	
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
