package com.dougnoel.sentinel.apis;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GET extends Action {
	private static final Logger log = LogManager.getLogger(GET.class); // Create a logger.
	
	public GET(String endpoint) {
		super(endpoint);
	}
	
	@Override
	public Response sendRequest(Request request, API api) throws IOException, URISyntaxException {
		URIBuilder builder = new URIBuilder(getURI(api.getURL()));
		builder.addParameter("access_token", api.getAuthToken());
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(builder.build());
		HttpResponse httpResponse = httpClient.execute(httpGet);
		
		Response response = new Response(httpResponse);
		log.trace("Response Code: {} Response: {}", response.getResponseCode(), response.getResponse());
		return response;
	}

}
