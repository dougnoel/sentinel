package com.dougnoel.sentinel.systems;

/**
 * Used to return a true/false value on whether or not an execution was successful,
 * along with any output.
 * 
 * @author dnoel
 *
 */
public class ExecutionResult {
	private boolean success;
	private String message;
	
	ExecutionResult() {
		this.success = false;
		this.message = "";
	}
	
	ExecutionResult(boolean result, String message) {
		this.success = result;
		this.message = message;
	}
	
	/**
	 * Returns true if the command was successful, otherwise false.
	 * @return boolean true if the command was successful, otherwise false.
	 */
	public boolean getSuccess() {
		return this.success;
	}
	
	public void success(boolean result) {
		this.success = result;
	}
	
	/**
	 * Returns any command line output if successful, otherwise returns the error message.
	 * @return String command line output if successful, otherwise returns the error message.
	 */
	public String getMessage() {
		return this.message;
	}

	public void message(String message) {
		this.message = message;
	}
	
	public void appendMessage(String text) {
		this.message += text;
	}
}
