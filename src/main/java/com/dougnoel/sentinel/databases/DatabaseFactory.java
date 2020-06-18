package com.dougnoel.sentinel.databases;

import java.util.HashMap;
/**
 * The DB Factory is a factory method that simply takes a string containing the name of a 
 * Database Object and returns the object to be worked on.
 */
public class DatabaseFactory {
	private static HashMap<String, Database> databases = new HashMap<>();	
	
	private DatabaseFactory() {
		//Exists only to defeat instantiation.
	}
	
	/**
	 * Returns the Database Object for the database name. This allows us to operate on datbases
	 * without knowing they exist when we write step definitions.
	 * 
	 * @param databaseName String the name of the database object
	 * @return Database the database object
	 */
	public static Database buildOrRetrieveDatabase(String databaseName) {
		Database database = databases.get(databaseName);
		if (database != null) {
			return database;
		} else {
			database = new Database(databaseName);
		}
		databases.put(databaseName, database);
		return database;
	}
}