package com.dougnoel.sentinel.apis;

import java.util.Map;

public class Request {

	protected String jsonRequest = null;
	protected Map<String,String> headers = null;
	protected Map<String,String> requestParameters = null;

	public Request() {
	}
	
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
		return jsonRequest = getHeaders() + getRequestParameters();
	}
	
	public String getRequest() {
		if (jsonRequest == null) {
			return buildRequest();
		}
		return jsonRequest;
	}

}
