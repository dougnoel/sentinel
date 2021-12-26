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
public class YAMLFileException extends SentinelException {
	private static final Logger log = LogManager.getLogger(YAMLFileException.class);
    private static final long serialVersionUID = 7430222710522100336L;
    private final File file;

    public YAMLFileException(File file) {
    	super();
        this.file = file;
        log.error(this.getMessage(), this);
    }
    
    public YAMLFileException(Throwable cause, File file) {
        super(cause);
        this.file = file;
        log.error(this.getMessage(), this);
    }
    
    public YAMLFileException(String message, File file) {
        super(message);
        this.file = file;
        log.error(this.getMessage(), this);
    }

    public YAMLFileException(String message, Throwable cause, File file) {
        super(message, cause);
        this.file = file;
        log.error(this.getMessage(), this);
    }
    
    @Override
    public String getMessage() {
    	if (this.getCause() == null)
    		return filePath();
    	
    	switch(this.getCause().getClass().getSimpleName()) {
    	case "JsonParseException":
    		return SentinelStringUtils.format("{} is not a valid YAML file. {}", filePath(), super.getMessage());
    	case "JsonMappingException":
    		return SentinelStringUtils.format("{} has incorrect formatting and cannot be read. {}", filePath(), super.getMessage());
    	case "FileNotFoundException":
    		return SentinelStringUtils.format("{} cannot be found in the specified location. {}", filePath(), super.getMessage());
    	case "IOException":
    		return SentinelStringUtils.format("{} cannot be opened. {}", filePath(), super.getMessage());
    	default:
    		return SentinelStringUtils.format("{} {}", filePath(), super.getMessage());
    	}
    }
    
	public File getFile() {
    	return file;
    }
	
	public String filePath() {
		return file.getAbsoluteFile().toString();
	}
}
