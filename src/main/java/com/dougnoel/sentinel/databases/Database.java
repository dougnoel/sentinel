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
    protected Connection conn = null;
    
    public Database(String databaseConnectionName) {
    	this.databaseConnectionName = databaseConnectionName;
    	loadDriver();
    }

    public String getName() {
        return databaseConnectionName;
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
	           DriverManager.getConnection("jdbc:mysql://localhost/test?" +
	                                       "user=minty&password=greatsqldb");
	
	        // Do something with the Connection
	
	    } catch (SQLException ex) {
	        // handle any errors
	        System.out.println("SQLException: " + ex.getMessage());
	        System.out.println("SQLState: " + ex.getSQLState());
	        System.out.println("VendorError: " + ex.getErrorCode());
	    }
    }
    
    public String query(String queryString) {
    	getConnection();
	    Statement stmt = null;
	    ResultSet rs = null;
	    String result = "";
	    try {
	        stmt = conn.createStatement();
	        rs = stmt.executeQuery(queryString);

	        // or alternatively, if you don't know ahead of time that
	        // the query will be a SELECT...

//	        if (stmt.execute(queryString)) {
//	            rs = stmt.getResultSet();
//	        }

	        result = rs.getString(1);
	    }
	    catch (SQLException ex){
	        // handle any errors
	        System.out.println("SQLException: " + ex.getMessage());
	        System.out.println("SQLState: " + ex.getSQLState());
	        System.out.println("VendorError: " + ex.getErrorCode());
	    }
	    finally {
	        // it is a good idea to release
	        // resources in a finally{} block
	        // in reverse-order of their creation
	        // if they are no-longer needed

	        if (rs != null) {
	            try {
	                rs.close();
	            } catch (SQLException sqlEx) { } // ignore

	            rs = null;
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
