package com.dougnoel.sentinel.exceptions;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.strings.SentinelStringUtils;

/**
 * Creating an unchecked YAMLFileException so that we can fail a test without failing the entire
 * program execution and create custom messages for all the failures that can happen reading a YAML file.
 * 
 * @author dougnoel
 *
 */
public class YAMLFileException extends RuntimeException {
	private static final Logger log = LogManager.getLogger(YAMLFileException.class);
    private static final long serialVersionUID = 7430222710522100336L;
    private static final String CONFIGURATION_FILE = "sentinel.yml";
    private final File file;

    /**
     * Creates a custom exception message and logs it to the error log.
     * 
     * @param file java.io.File the File that caused the exception
     */
    public YAMLFileException(File file) {
    	super();
        this.file = file;
        logMessage();
    }
    
    /**
     * Creates a custom exception message and logs it to the error log.
     * 
     * @param cause Throwable the exception that was thrown that caused this exception to be thrown
     * @param file java.io.File the File that caused the exception
     */
    public YAMLFileException(Throwable cause, File file) {
        super(cause);
        this.file = file;
        logMessage();
    }
    
    /**
     * Creates a custom exception message and logs it to the error log.
     * 
     * @param message String the custom text to add to the custom exception message
     * @param file java.io.File the File that caused the exception
     */
    public YAMLFileException(String message, File file) {
        super(message);
        this.file = file;
        logMessage();
    }

    /**
     * Creates a custom exception message and logs it to the error log.
     * 
     * @param message String the custom text to add to the custom exception message
     * @param cause Throwable the exception that was thrown that caused this exception to be thrown
     * @param file java.io.File the File that caused the exception
     */
    public YAMLFileException(String message, Throwable cause, File file) {
        super(message, cause);
        this.file = file;
        logMessage();
    }
    
    /**
     * Returns a custom error message based on the Exception that initially caused this to be thrown.
     * 
     * @return String the custom error message
     */
    @Override
    public String getMessage() {
    	if (this.getCause() == null) {
    		return nullSafeMessage();
    	}
    	switch(this.getCause().getClass().getSimpleName()) {
    	case "JsonParseException":
    		return SentinelStringUtils.format("{} is not a valid YAML file. {}", filePath(), super.getMessage());
    	case "JsonMappingException":
    		return SentinelStringUtils.format("{} has incorrect formatting and cannot be read. {}", filePath(), super.getMessage());
    	case "AccessDeniedException":
    		return SentinelStringUtils.format("{} could not be accessed. Please ensure the file can be read by the current user and is not password protected. {}", 
    				filePath(), super.getMessage());
    	case "FileNotFoundException":
    		return SentinelStringUtils.format("{} cannot be found in the specified location. {}", filePath(), super.getMessage());
    	case "IOException":
    		return SentinelStringUtils.format("{} cannot be opened. {}", filePath(), super.getMessage());
    	default:
    		return nullSafeMessage();
    	}
    }
    
    /**
     * Returns a message without "null" showing up in it.
     * 
     * @return String the message to send
     */
    private String nullSafeMessage() {
		String message = super.getMessage();
		if (message == null) {
			return filePath();
		}
		return filePath() + " " + message;
    }
    
    /**
     * Returns the File set when this exception was created.
     * 
     * @return java.io.File the File that caused the exception
     */
	public File getFile() {
    	return file;
    }
	
	/**
	 * Returns the absolute path to the file or directory that threw the error.
	 * 
	 * @return String the absolute path of the File that caused the exception
	 */
	public String filePath() {
		return file.getAbsoluteFile().toString();
	}
	
	/**
	 * Logs the error if it is not a missing sentinel.yml configuration file.
	 * Logs missing sentinel.yml file if DEBUG mode is on.
	 * Logs the stack trace for all but a missing sentinel.yml file if TRACE mode is on.
	 */
	public void logMessage() {
		if(!filePath().contains(CONFIGURATION_FILE) && !getCause().getClass().getSimpleName().equals("FileNotFoundException")) {
			if (log.isTraceEnabled()) 
				log.trace(this.getMessage(), this);
			else
				log.error(this.getMessage());
		}
		log.debug(this.getMessage());
	}
}