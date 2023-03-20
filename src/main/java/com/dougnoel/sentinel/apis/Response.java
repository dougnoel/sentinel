package com.dougnoel.sentinel.apis;

import java.io.IOException;
import java.time.Duration;

import org.apache.http.HttpResponse;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

/**
 * Wrapper for an hhtp response for testing the response.
 * @author dougnoel
 *
 */
public class Response {
	
	protected HttpResponse httpResponse;
	protected String jsonResponse;
	protected Duration responseTime;
	
	/**
	 * 
	 * @param httpResponse HttpResponse the aPI call response used to create this object
	 * @throws IOException if the parsing fails
	 */
	public Response(HttpResponse httpResponse) throws IOException {
		this.httpResponse = httpResponse;
		this.jsonResponse = SentinelStringUtils.inputStreamToString(this.httpResponse.getEntity().getContent()); //This has to be done when we first get the response because once we read the stream, it is gone.
	}

	/**
	 * Returns the http response as a String
	 * @return String the http response
	 */
	public String getResponse() {
		return jsonResponse;
	}
	
	/**
	 * Returns the response code from the response
	 * @return int the status of the response
	 */
	public int getResponseCode() {
		return httpResponse.getStatusLine().getStatusCode();
	}
	
	/**
	 * Sets the amount of time that the response took to get.
	 * @param duration
	 */
	public void setResponseTime(Duration duration) {
		responseTime = duration;
	}
	
	/**
	 * Returns the amount of time the response took to get.
	 * @return Duration the response time
	 */
	public Duration getReponseTime() {
		return responseTime;
	}
}
