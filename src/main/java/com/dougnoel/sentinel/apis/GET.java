package com.dougnoel.sentinel.apis;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GET extends Action {
	private static final Logger log = LogManager.getLogger(Action.class.getName()); // Create a logger.
	
	public GET(String endpoint) {
		super(endpoint);
	}
	
//	public Response sendRequestOld(Request request, URL url) throws ClientProtocolException, IOException {
//		String uri = getURI(url);
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		HttpGet httpGet = new HttpGet(uri);
//		
//		ResponseHandler<String> responseHandler = new BasicResponseHandler();
//		Response response = new Response(httpclient.execute(httpGet, responseHandler));
//		log.info("Response Handler: {}", responseHandler);
//		return response;
//	}
	
	@Override
	public Response sendRequest(Request request, API api) throws ClientProtocolException, IOException, URISyntaxException {
		URIBuilder builder = new URIBuilder(getURI(api.getURL()));
		builder.addParameter("access_token", api.getAuthToken());
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(builder.build());
		HttpResponse httpResponse = httpClient.execute(httpGet);
		
		Response response = new Response(httpResponse);
		log.trace("Response Code: {} Response: {}", response.getResponseCode(), response.getResponse());
		return response;
	}
	
//	public int getResponseCode(Request request, API api) throws ClientProtocolException, IOException, URISyntaxException {
//		URIBuilder builder = new URIBuilder(api.getURI());
//		builder.addParameter("access_token", api.getAuthToken());
//		
//		HttpClient httpClient = HttpClientBuilder.create().build();
//		HttpGet httpGet = new HttpGet(builder.build());
//		ResponseHandler<String> responseHandler = new BasicResponseHandler();
//		HttpResponse httpResponse = httpClient.execute(httpGet);
//		
//		Response response = new Response(httpResponse);
//		response.addJsonResponse(httpClient.execute(httpGet, responseHandler));
//		log.info("Response Code: {} Response: {}", response.getResponseCode(), response.getResponse());
//		response.getStatusLine().getStatusCode();
//		return response;
//	}
	
//	private User getUser(@NotNull Token token) throws IOException, URISyntaxException {
//
//		URIBuilder builder = new URIBuilder(getURI(url));
//		builder.addParameter("access_token", token.getAccessToken());
//
//		HttpClient httpClient = HttpClientBuilder.create().build();
//		HttpGet httpGet = new HttpGet(builder.build());
//		
//		org.apache.http.HttpResponse response = httpClient.execute(httpGet);
//		int statusCode = response.getStatusLine().getStatusCode();
//		String responseBody = response.get
//		InputStream inputStream = response.getEntity().getContent();
//
//		if (HttpUtilities.success(statusCode)) {
//			User user = gson.fromJson(new InputStreamReader(inputStream), User.class);
//			user.setToken(token);
//			return user;
//		}
//
//		throw new ApiException(HttpStatus.valueOf(statusCode));
//	}
	
//	public static void doGetAndPrintResultToConsole(String APIURL) throws ClientProtocolException, IOException {
//		CloseableHttpClient httpClient = HttpClients.createDefault();
//		HttpGet httpGet = new HttpGet(APIURL);
//
//		HttpUriRequest request = RequestBuilder.get()
//		  .setUri(APIURL)
//		  .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
//		  .setHeader(HttpHeaders.AUTHORIZATION, StringUtils.format("Bearer {}",jwt))
//		  .build();
//		
//		ResponseHandler<String> responseHandler = new BasicResponseHandler();
//		String responseBody = httpClient.execute(request, responseHandler);
//		
//		System.out.println(responseBody);
//	}

//	public void verifyResponseAndPrintToConsole() throws ClientProtocolException, IOException {
//	    CloseableHttpClient httpClient = HttpClients.createDefault();
//		HttpGet httpGet = new HttpGet("https://jsonplaceholder.typicode.com/todos");
//		
//		ResponseHandler<String> responseHandler = new BasicResponseHandler();
//		String responseBody = httpClient.execute(httpGet, responseHandler);
//		
//		final ObjectMapper mapper = new ObjectMapper();
//		com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(responseBody);
//		
//		for (com.fasterxml.jackson.databind.JsonNode child : root) {
//			String userID = child.at("/userId").asText();
//			System.out.println("Test: " + userID);
//		}
//		
//		System.out.println("Does it have a userID: " + root.findValue("1"));
//	}
}
