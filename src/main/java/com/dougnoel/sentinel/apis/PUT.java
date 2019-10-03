package com.dougnoel.sentinel.apis;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;

public class PUT extends Action {

	public PUT(String endpoint) {
		super(endpoint);
	}
	
	@Override
	public Response sendRequest(Request request, API api) throws ClientProtocolException, IOException {
		return null;
	}
	/**
	 * Ensures that headers, URL and request are not null;
	 * @return response from the URL
	 */
//	public String sendRequest(URL url) {
//		String response = null;
////		if (headers.isEmpty()) { // || requests.isEmpty() || url == null) {
////			response = "Missing information. Cannot send request.";
////		} else {
//			//Send request
//			//process response
//			response = responseCode + jsonResponse;
////		}
//		return response;
//	}

}
