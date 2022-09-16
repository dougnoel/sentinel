package com.dougnoel.sentinel.apis.actions;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.apis.API;
import com.dougnoel.sentinel.apis.APIManager;
import com.dougnoel.sentinel.apis.Request;
import com.dougnoel.sentinel.apis.Response;

public class GET extends Action {
	private static final Logger log = LogManager.getLogger(GET.class); // Create a logger.
	
	public GET(String endpoint) {
		super(endpoint);
	}
	
	@Override
	public Response sendRequest(Request request) throws IOException, URISyntaxException {
		API api = APIManager.getAPI();
		
		//Build the URI
		URIBuilder uriBuilder = new URIBuilder(api.getURI());
		uriBuilder.addParameter("access_token", api.getAuthToken());
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		//Build the request
		HttpGet httpGet = request.getRequest(uriBuilder.build());
		
		//Get the response
		Response response = new Response(httpClient.execute(httpGet));
		log.trace("Response Code: {} Response: {}", response.getResponseCode(), response.getResponse());
		return response;
	}

}
