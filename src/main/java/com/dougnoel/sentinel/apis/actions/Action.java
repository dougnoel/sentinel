package com.dougnoel.sentinel.apis.actions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import com.dougnoel.sentinel.apis.Request;
import com.dougnoel.sentinel.apis.Response;

/**
 * An abstract class that defines an action for an API object.
 * 
 * Implemented by GET, PUT, and POST concrete classes. For sending a request and receiving a response. Actual
 * requests and responses are stored in Request and Response object collections.
 * 
 * @author Doug Noël
 *
 */
public abstract class Action {
	protected String endpoint = null;
	
	protected Action(String endpoint) {
		this.endpoint = endpoint;
	}
		
//	/**
//	 * Returns a URL as a String with the endpoint attached. 
//	 * @param url java.net.URL URL of the API
//	 * @return String the URL with the Action's endpoint attached.
//	 */
//	public String getURI(URL url) {
//		String uri = url.toString();
//		return (uri.endsWith("/") ? uri : uri + "/") + endpoint;
//	}
	
	/**
	 * Returns a response from a given api using a given request.
	 * @param request Request the Sentinel Request object to create and send the request
	 * @return Response the Sentinel Response object returned for evaluation
	 * @throws IOException if the HttpClient execution fails or reading the response IOStream fails
	 * @throws URISyntaxException if building the URI fails
	 */
	public abstract Response sendRequest(Request request) throws IOException, URISyntaxException;
}
