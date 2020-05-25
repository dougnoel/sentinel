package com.dougnoel.sentinel.apis;

import java.io.IOException;

import org.apache.http.HttpResponse;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

public class Response {
	
	protected HttpResponse response;
	protected String jsonResponse;
	
	public Response(HttpResponse httpResponse) throws IOException {
		this.response = httpResponse;
		this.jsonResponse = SentinelStringUtils.inputStreamToString(response.getEntity().getContent()); //This has to be done when we first get the response because once we read the stream, it is gone.
	}
	
	public void addJsonResponse(String jsonResponse) {
		this.jsonResponse = jsonResponse;
	}

	public String getResponse() {
		return jsonResponse;
	}
	
	public Integer getResponseCode() {
		return response.getStatusLine().getStatusCode();
	}
	
}
