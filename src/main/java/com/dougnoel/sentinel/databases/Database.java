package com.dougnoel.sentinel.databases;

import java.net.URL;

/**
 * Database class to contain connection details.
 */
public class Database {

    protected URL url;    
    private String databaseName;
    
    public Database(String databaseName) {
    	this.databaseName = databaseName;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getName() {
        return databaseName;
    }
}
