package com.dougnoel.sentinel.apis;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
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

import com.dougnoel.sentinel.enums.RequestType;
import com.dougnoel.sentinel.exceptions.IOException;

public class Request {
	private static final Logger log = LogManager.getLogger(Request.class.getName()); // Create a logger.

	private HttpRequestBase request = null;
	private List<NameValuePair> parameters = new ArrayList<>();
	private StringEntity body = null;
	
	private void reset() {
		parameters.clear();
		body = null;
		request = null;
	}
	
	/**
	 * Set a parameter and its value for a request. They will show up as part of the query string in the API request.
	 * @param parameter String the parameter being passed
	 * @param value String the value to be passed
	 */
	public void addParameter(String parameter, String value) {
		parameters.add(new BasicNameValuePair(parameter, value));
	}
	
	/**
	 * Returns a URI that includes any parameters if it is set. Otherwise it returns the existing URI.
	 * @param request HttpRequestBase the request to which to add the query string
	 * @return HttpRequestBase the new request object
	 */
	private HttpRequestBase buildURI() {
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
	 * Sets this as a json request.
	 */
	private void setHeaders() {
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
	}
	
	/**
	 * Creates a StringEntity to hold the json body.
	 * @param body String the JSON to encode.
	 */
	public void setBody(String body) {
		try {
			this.body = new StringEntity(body);
		} catch (UnsupportedEncodingException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * Construct a request, send it to the active API, and store the response for retrieval.
	 * Parameterization is handled at the cucumber step level.
	 * 
	 * @param type com.dougnoel.sentinel.enums.RequestType the type of request to send
	 * @param endpoint the endpoint to send the request
	 */
	public void createAndSendRequest(RequestType type, String endpoint) {
		try {
			switch(type) {
			case DELETE:
				request = new HttpDelete(APIManager.getAPI().getURIBuilder("/" + endpoint).build());
				break;
			case GET:
				request = new HttpGet(APIManager.getAPI().getURIBuilder("/" + endpoint).build());
				break;
			case POST:
				request = new HttpPost(APIManager.getAPI().getURIBuilder("/" + endpoint).build());
				((HttpEntityEnclosingRequestBase) request).setEntity(body);
				break;
			case PUT:
				request = new HttpPut(APIManager.getAPI().getURIBuilder("/" + endpoint).build());
				((HttpEntityEnclosingRequestBase) request).setEntity(body);
				break;
			}
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
		setHeaders();
		buildURI();
	    sendRequest();
	}
	
	/**
	 * Send the request, store the response for later retrieval, and reset the request so it can be used again
	 * by the API for another request.
	 */
	private void sendRequest() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		Response response;
		try {
			response = new Response(httpClient.execute(request));
		} catch (java.io.IOException e) {
			throw new IOException(e);
		}
		log.trace("Response Code: {} Response: {}", response.getResponseCode(), response.getResponse());
		APIManager.setResponse(response);
		reset();
	}

}
