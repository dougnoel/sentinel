package com.dougnoel.sentinel.databases;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.exceptions.PageNotFoundException;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
/**
 * The Page Factory is a factory method that simply takes a string containing the name of a 
 * Page Object and returns the object to be worked on. It handles searching packages for page definitions.
 */
public class DatabaseFactory {
	private static final Logger log = LogManager.getLogger(DatabaseFactory.class); // Create a logger.
	private static HashMap<String, Database> databases = new HashMap<>();
	private static String[] pageObjectPackagesList = null;
	private static final String PAGE_NOT_FOUND_ERROR_MESSAGE = "The page you want to test could not be built. At least one Page object package is required to run a test. Please add a pageObjectPackages property to your conf/sentinel.yml configuration file and try again.";
	
	
	private DatabaseFactory() {
		//Exists only to defeat instantiation.
	}
	
	//TODO: Throw Exceptions if page object creation fails
	/**
	 * Returns a page object if it exists in the package searched.
	 * @param pageName String the name of the page object class to instantiate
	 * @param packageName String the name of the package to search
	 * @return Page the page object if it exists, otherwise null
	 */
	private static Page findPageInPackage(String pageName, String packageName) {
		Page page = null;
		try {
			page = (Page) Class.forName(packageName + "." + pageName).getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			String errorMessage = SentinelStringUtils.format("Exception suppressed during {}.{} Page Object creation.\n{}", packageName, pageName, e);
			log.trace(errorMessage);
		}
		
		
		return page;
	}
	
	/**
	 * Returns the Page Object for the page name. This allows us to operate on pages
	 * without knowing they exist when we write step definitions.
	 * <p>
	 * Searches any optional page object packages set with the pageObjectPackages system property, and 
	 * then searches the defaultPackageName value.
	 * <p>
	 * <b>Example:</b>
	 * <p>
	 * <code>System.setProperty("pageObjectPackages", "pages.SharedComponent,pages.UserAdminTool");</code>
	 * 
	 * @param pageName String the name of the page in
	 *                 <a href="https://en.wikipedia.org/wiki/Camel_case">Pascal
	 *                 case</a>
	 * @return Page the specific page object cast as a generic page object
	 */
	public static Page buildOrRetrievePage(String pageName) {
		Page page = databases.get(pageName);
		if (page != null) {
			return page;
		} else {
			if (pageObjectPackagesList == null) {
				pageObjectPackagesList = ConfigurationManager.getPageObjectPackageList();
				if(pageObjectPackagesList == null) {
					throw new PageNotFoundException(PAGE_NOT_FOUND_ERROR_MESSAGE);
				}
			}

			for (String pageObjectPackage : pageObjectPackagesList) {
				log.trace("pageObjectPackage: {}", pageObjectPackage);
				page = findPageInPackage(pageName, pageObjectPackage);
				if (page != null) {
					break; // If we have a page object, stop searching.
				}
			}
			//Added ability to create a page object without a java file, but still allow default functionality for now.
			page = new Page(pageName);
		}
		databases.put(pageName, page);
		return page;
	}

}