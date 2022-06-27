package com.dougnoel.sentinel.apis;

import java.io.IOException;

public class POST extends Action {

	public POST(String endpoint) {
		super(endpoint);
	}
	
	@Override
	public Response sendRequest(Request request, API api) throws IOException {
		return null;
	}
	
	/**
	 * Ensures that headers, URL and request are not null;
	 * @return response from the URL
	 */

}
