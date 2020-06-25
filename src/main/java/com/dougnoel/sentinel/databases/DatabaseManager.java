package com.dougnoel.sentinel.databases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Database Manager is a singleton class that manages what database steps should use for
 * connection.
 */
public class DatabaseManager {
	private static final Logger log = LogManager.getLogger(DatabaseManager.class);
	private static Database database = null;
	private static String databaseInUse = null;

	private DatabaseManager() {
		// Exists only to defeat instantiation.
	}

	public static void setDatabase(String databaseName) {
		DatabaseManager.database = DatabaseFactory.buildOrRetrieveDatabaseConnection(databaseName);
	}
	
	public static void useDatabase(String databaseName) {
		DatabaseManager.databaseInUse = databaseName;
	}

}