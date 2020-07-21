package com.dougnoel.sentinel.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database class to contain connection details.
 */
public class Database {

    protected String databaseConnectionName;
    protected String currentDatabaseName;
    protected Connection conn = null;
    
    public Database(String databaseConnectionName) {
    	this.databaseConnectionName = databaseConnectionName;
    	loadDriver();
    }

    public String getConnectionName() {
        return databaseConnectionName;
    }
    
    public void setCurrentDatabaseName(String currentDatabaseName) {
    	this.currentDatabaseName = currentDatabaseName;
    }
    
    public String getCurrentDatabaseName() {
    	return currentDatabaseName;
    }
    
    @SuppressWarnings("deprecation")
	public void loadDriver() {
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        }
    }
    
    public void getConnection() {
	    try {
	        conn =
	           DriverManager.getConnection("jdbc:mysql://" + 
	        		   						databaseConnectionName + "/" + 
	        		   						currentDatabaseName + "?" +
	        		   						"user=testuser&password=PoorPassword" +
	                                       	"&serverTimezone=UTC&useLegacyDatetimeCode=false");
	
	        // Do something with the Connection
	
	    } catch (SQLException ex) {
	        // handle any errors
	        System.out.println("getConnection SQLException: " + ex.getMessage());
	        System.out.println("getConnection SQLState: " + ex.getSQLState());
	        System.out.println("getConnection VendorError: " + ex.getErrorCode());
	    }
    }
    
    public String query(String queryString) {
    	getConnection();
	    Statement stmt = null;
	    ResultSet resultSet = null;
	    String result = "";
	    try {
	        stmt = conn.createStatement();
	        resultSet = stmt.executeQuery(queryString);

	        // or alternatively, if you don't know ahead of time that
	        // the query will be a SELECT...

//	        if (stmt.execute(queryString)) {
//	            rs = stmt.getResultSet();
//	        }

//	        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
//	        final int columnCount = resultSetMetaData.getColumnCount();
//
//	        while (resultSet.next()) {
//	            Object[] values = new Object[columnCount];
//	            for (int i = 1; i <= columnCount; i++) {
//	                values[i - 1] = resultSet.getObject(i);
//	            }
//	            result += values;
//	        }
	        
	        resultSet.next();
	        result = resultSet.getString(1);
	    }
	    catch (SQLException ex){
	        // handle any errors
	        System.out.println("query SQLException: " + ex.getMessage());
	        System.out.println("query SQLState: " + ex.getSQLState());
	        System.out.println("query VendorError: " + ex.getErrorCode());
	    }
	    finally {
	        // it is a good idea to release
	        // resources in a finally{} block
	        // in reverse-order of their creation
	        // if they are no-longer needed

	        if (resultSet != null) {
	            try {
	            	resultSet.close();
	            } catch (SQLException sqlEx) { } // ignore

	            resultSet = null;
	        }

	        if (stmt != null) {
	            try {
	                stmt.close();
	            } catch (SQLException sqlEx) { } // ignore

	            stmt = null;
	        }
	        try {
	        	conn.close();
	        }
		    catch (SQLException ex){
		        // handle any errors
		        System.out.println("SQLException: " + ex.getMessage());
		        System.out.println("SQLState: " + ex.getSQLState());
		        System.out.println("VendorError: " + ex.getErrorCode());
		    }
	    }
	    return result;
    }
}
