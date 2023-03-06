package com.dougnoel.sentinel.apis;

import java.util.ArrayList;

/**
 * Stores a set of responses in order so that they can be retrieved based on when they were created.
 * @author dougnoel@gmail.com
 *
 */
public class ResponseSet {
	protected ArrayList<Response> responses = new ArrayList<Response>();
	
	/**
	 * Adds a response object to the list of responses we are tracking.
	 * @param response the response to store
	 */
	public void add(Response response) {
		responses.add(response);
	}
	
	/**
	 * Returns the most recent response added to the list.
	 * @return Response the most recent response added to the list 
	 */
	public Response getLatest() {
		return responses.get(-1);
	}

	/**
	 * Returns the response added before the most recent response added.
	 * Used for comparing the current response to the previous one made.
	 * @return Response the most recent response added to the list 
	 */
	public Response getPrevious() {
		return responses.get(-2);
	}
	
	/**
	 * Returns the response stored based on a 0-based index.
	 * <br>0 returns the first response.
	 * <br>-1 returns the last response.
	 * @param index int the order the response was tored in the list
	 * @return rEsponse the requested response from the list
	 */
	public Response get(int index) {
		return responses.get(index);
	}
}