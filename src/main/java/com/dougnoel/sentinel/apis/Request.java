package com.dougnoel.sentinel.apis;

import java.net.URI;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;

public class Request {

	protected String jsonRequest = null;
	protected Map<String,String> headers = null;
	protected Map<String,String> requestParameters = null;

	/**
	 * Construct a blank request object that will need to be created
	 * with headers and parameters.
	 */
	public Request() {
	}
	
	/**
	 * Construct a request object by passing in a pre-created JSON request
	 * to pass.
	 * 
	 * @param jsonRequest
	 */
	public Request(String jsonRequest) {
		this.jsonRequest = jsonRequest;
	}
	
	public void addHeader(String key, String value) {
		headers.put(key, value);
	}
	
	public String getHeaders() {
		return headers.toString();
	}
	
	public void addRequestParameter(String key, String value) {
		requestParameters.put(key, value);
	}
	
	public String getRequestParameters() {
		return requestParameters.toString();
	}
	
	public String buildRequest() {
		jsonRequest = getHeaders() + getRequestParameters();
		return jsonRequest;
	}
	
	public HttpGet constructGetRequest(URI uri) {
		HttpGet httpGet = new HttpGet(uri);
		if (jsonRequest == null) {
			return buildRequest();
		}
		return httpGet;
	}

}
