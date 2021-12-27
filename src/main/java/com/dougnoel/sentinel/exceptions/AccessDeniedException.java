package com.dougnoel.sentinel.exceptions;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Creating an unchecked AccessDeniedException so that we can fail a test without failing the entire
 * program execution.
 * 
 * @author dougnoel
 *
 */
public class AccessDeniedException extends RuntimeException {
	private static final Logger log = LogManager.getLogger(AccessDeniedException.class);
    private static final long serialVersionUID = 8461275760821180367L;
    private final File file;

    /**
     * Creates a custom exception message and logs it to the error log.
     * 
     * @param file java.io.File the File that caused the exception
     */
    public AccessDeniedException(File file) {
    	super();
        this.file = file;
        log.error(this.getMessage(), this);
    }
    
    /**
     * Creates a custom exception message and logs it to the error log.
     * 
     * @param cause Throwable the exception that was thrown that caused this exception to be thrown
     * @param file java.io.File the File that caused the exception
     */
    public AccessDeniedException(Throwable cause, File file) {
        super(cause);
        this.file = file;
        log.error(this.getMessage(), this);
    }
    
    /**
     * Creates a custom exception message and logs it to the error log.
     * 
     * @param message String the custom text to add to the custom exception message
     * @param file java.io.File the File that caused the exception
     */
    public AccessDeniedException(String message, File file) {
        super(message);
        this.file = file;
        log.error(this.getMessage(), this);
    }

    /**
     * Creates a custom exception message and logs it to the error log.
     * 
     * @param message String the custom text to add to the custom exception message
     * @param cause Throwable the exception that was thrown that caused this exception to be thrown
     * @param file java.io.File the File that caused the exception
     */
    public AccessDeniedException(String message, Throwable cause, File file) {
        super(message, cause);
        this.file = file;
        log.error(this.getMessage(), this);
    }
    
    /**
     * Returns a custom error message.
     * 
     * @return String the custom error message
     */
    @Override
    public String getMessage() {
    	return super.getMessage() + " Access denied for file path: " + filePath();
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
}
