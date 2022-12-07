package com.dougnoel.sentinel.apis;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NotFoundException;

import com.dougnoel.sentinel.exceptions.IOException;

/**
 * Tracks which API is currently being used and requests the APIFactory create it if it does not exist.
 * @author dougnoel@gmail.com
 *
 */
public class APIManager {
	private static final Logger log = LogManager.getLogger(APIManager.class.getName()); // Create a logger.
	//Only one API should be in use at a time. We are consciously not multi-threading.
	private static List<NameValuePair> parameters = new ArrayList<>();
	private static API api = null;
	private static StringEntity body = null;
	private static Request request = null;
	private static Response response = null;
	
	private APIManager() {
		// Exists only to defeat instantiation.
	}
	
	public static void reset() {
		api = null;
		body = null;
		parameters.clear();
		request = null;
		response = null;
	}
	
	/**
	 * Stores an API using the passed API name to instantiate it.
	 * @param apiName String the name of the sentinel API object to create and store
	 */
	public static void setAPI(String apiName) {
		try {
			APIManager.api = APIFactory.buildOrRetrieveAPI(apiName);
		} catch (NullPointerException npe) {
			api = null;
		}
	}
	
	/**
	 * Returns the currently active API
	 * @return API Currently selected API by the tester.
	 */
	public static API getAPI() {
		if (api == null)
			throw new NotFoundException("API not set yet. It must be created to before it can be used.");
		return APIManager.api;
	}
	
	/**
	 * Creates a StringEntity to hold the json body.
	 * @param body String the JSON to encode.
	 */
	public static void setBody(String body) {
		try {
			APIManager.body = new StringEntity(body);
		} catch (UnsupportedEncodingException e) {
			throw new IOException(e);
		}
	}
	
	public static void addParameter(String parameter, String value) {
		parameters.add(new BasicNameValuePair(parameter, value));
	}
	
	/**
	 * Returns the body of the message as a StringEntity
	 * @return StringEntity the request body
	 */
	public static StringEntity getBody() {
		return APIManager.body;
	}

	public static Request getRequest() {
		return request;
	}

	public static void setRequest(String requestString) {
		Request request = new Request(requestString);
		APIManager.request = request;
	}

	public static Response getResponse() {
		return response;
	}

	public static void setResponse(Response response) {
		APIManager.response = response;
	}
	
	private static void sendRequest(HttpRequestBase request) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		//Get the response
		Response response;
		try {
			response = new Response(httpClient.execute(request));
		} catch (java.io.IOException e) {
			throw new IOException(e);
		}
		log.trace("Response Code: {} Response: {}", response.getResponseCode(), response.getResponse());
		APIManager.setResponse(response);
	}
	
	private static HttpRequestBase buildURI(HttpRequestBase request) {
	    if (!parameters.isEmpty()) {
			URI uri;
			try {
				uri = new URIBuilder(request.getURI())
				  .addParameters(parameters)
				  .build();
				request.setURI(uri);
			} catch (URISyntaxException e) {
				throw new IOException(e);
			}
	    }

	    log.trace("URI Constructed: {}", request.getURI());
		return request;
	}
	
	/**
	 * POST the existing API call to the currently selected API.
	 * 
	 * @param endpoint String the endpoint to use
	 */
	public static void POST(String endpoint) {
		//Build the request
		HttpPost httpPost;
		try {
			httpPost = new HttpPost(APIManager.getAPI().getURIBuilder("/" + endpoint).build());
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}

	    httpPost.setEntity(APIManager.getBody());
	    httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-type", "application/json");
	    
	    sendRequest(buildURI(httpPost));
	}
	
	/**
	 * PUT the existing API call to the currently selected API.
	 * 
	 * @param endpoint String the endpoint to use
	 */
	public static void PUT(String endpoint) {
		//Build the request
		HttpPut httpPut;
		try {
			httpPut = new HttpPut(APIManager.getAPI().getURIBuilder("/" + endpoint).build());
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}

	    httpPut.setEntity(APIManager.getBody());
	    httpPut.setHeader("Accept", "application/json");
	    httpPut.setHeader("Content-type", "application/json");
	    
	    sendRequest(buildURI(httpPut));
	}

	public static void DELETE(String endpoint) {
		//Build the request
		HttpDelete httpDelete;
		try {
			httpDelete = new HttpDelete(APIManager.getAPI().getURIBuilder("/" + endpoint).build());
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
		
		sendRequest(buildURI(httpDelete));
	}
	
	public static void DELETE(String endpoint, String parameter) {
		//Build the request
		HttpDelete httpDelete;
		try {
			httpDelete = new HttpDelete(APIManager.getAPI().getURIBuilder("/" + endpoint + "/" + parameter).build());
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
		
		sendRequest(buildURI(httpDelete));
	}
	
	public static void GET(String endpoint) {
		//Build the request
		HttpGet httpGet;
		try {
			httpGet = new HttpGet(APIManager.getAPI().getURIBuilder("/" + endpoint).build());
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
		
		sendRequest(buildURI(httpGet));
	}
	
	public static void GET(String endpoint, String parameter) {
		//Build the request
		HttpGet httpGet;
		try {
			httpGet = new HttpGet(APIManager.getAPI().getURIBuilder("/" + endpoint + "/" + parameter).build());
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
		
		sendRequest(buildURI(httpGet));
	}
}