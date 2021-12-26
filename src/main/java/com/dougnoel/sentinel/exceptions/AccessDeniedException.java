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
public class AccessDeniedException extends SentinelException {
	private static final Logger log = LogManager.getLogger(AccessDeniedException.class);
    private static final long serialVersionUID = 8461275760821180367L;
    private final File file;

    public AccessDeniedException(File filePath) {
    	super();
        this.file = filePath;
        log.error(this.getMessage(), this);
    }
    
    public AccessDeniedException(Throwable cause, File filePath) {
        super(cause);
        this.file = filePath;
        log.error(this.getMessage(), this);
    }
    
    public AccessDeniedException(String message, File filePath) {
        super(message);
        this.file = filePath;
        log.error(this.getMessage(), this);
    }

    public AccessDeniedException(String message, Throwable cause, File filePath) {
        super(message, cause);
        this.file = filePath;
        log.error(this.getMessage(), this);
    }
    
    @Override
    public String getMessage() {
    	return super.getMessage() + " Access denied for file path: " + filePath();
    }
    
	public File getFile() {
    	return file;
    }
	
	public String filePath() {
		return file.getAbsoluteFile().toString();
	}
}
