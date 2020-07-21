package com.dougnoel.sentinel.databases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Database Manager is a singleton class that manages what database steps should use for
 * connection.
 */
public class DatabaseManager {
	private static final Logger log = LogManager.getLogger(DatabaseManager.class);
	private static Database databaseConnection = null;

	private DatabaseManager() {
		// Exists only to defeat instantiation.
	}

	public static void setDatabaseConnection(String databaseName) {
		DatabaseManager.databaseConnection = DatabaseFactory.buildOrRetrieveDatabaseConnection(databaseName);
	}
	
	public static void useDatabase(String currentDatabaseName) {
		getCurrentDatabaseConnection().setCurrentDatabaseName(currentDatabaseName);
	}
	
	public static Database getCurrentDatabaseConnection() {
		return databaseConnection;
	}
	
	public static String getDatabaseInUse() {
		return getCurrentDatabaseConnection().getCurrentDatabaseName();
	}

}