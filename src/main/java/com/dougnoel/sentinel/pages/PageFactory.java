package com.dougnoel.sentinel.pages;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.exceptions.ConfigurationNotFoundException;
import com.dougnoel.sentinel.exceptions.PageNotFoundException;
/**
 * The Page Factory is a factory method that simply takes a string containing the name of a 
 * Page Object and returns the object to be worked on. It handles searching packages for page definitions.
 */
public class PageFactory {
	private static final Logger log = LogManager.getLogger(PageFactory.class); // Create a logger.
	private static HashMap<String, Page> pages = new HashMap<String, Page>();
	private static String[] pageObjectPackagesList = null;
	
	private PageFactory() {
		//Exists only to defeat instantiation.
	}
	
	//TODO: Throw Exceptions if page object creation fails
	//TODO: Need to find Java 11 way of instantiation of objects using reflection
	/**
	 * Returns a page object if it exists in the package searched.
	 * @param pageName String the name of the page object class to instantiate
	 * @param packageName String the name of the package to search
	 * @return Page the page object if it exists, otherwise null
	 */
	@SuppressWarnings("deprecation")
	private static Page findPageInPackage(String pageName, String packageName) {
		Page page = null;
		try {
			page = (Page) Class.forName(packageName + "." + pageName).newInstance();
		} catch (InstantiationException e) {
			log.trace("{}.{} Page Object creation failed.", packageName, pageName);
			log.trace("java.lang.InstantiationException: {}", e.getMessage());
		} catch (IllegalAccessException e) {
			log.trace("{}.{} Page Object creation failed.", packageName, pageName);
			log.trace("java.lang.IllegalAccessException: {}", e.getMessage());
		} catch (ClassNotFoundException e) {
			log.trace("{}.{} Page Object creation failed.", packageName, pageName);
			log.trace("java.lang.ClassNotFoundException: {}", e.getMessage());
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
	 * @throws PageNotFoundException if page could not be built or retrieved.
	 * @throws ConfigurationNotFoundException if the value is not found in the configuration file
	 */
	public static Page buildOrRetrievePage(String pageName) throws PageNotFoundException, ConfigurationNotFoundException {
		Page page = pages.get(pageName);
		final String errorMessage = "The page you want to test could not be built. At least one Page object package is required to run a test. Please add a pageObjectPackages property to your conf/sentinel.yml configuration file and try again.";
		if (page != null) {
			return page;
		} else {
			if (pageObjectPackagesList == null) {
				pageObjectPackagesList = ConfigurationManager.getPageObjectPackageList();
				if(pageObjectPackagesList == null) {
					throw new PageNotFoundException(errorMessage);
				}
			}

			for (String pageObjectPackage : pageObjectPackagesList) {
				log.trace("pageObjectPackage: " + pageObjectPackage);
				page = findPageInPackage(pageName, pageObjectPackage);
				if (page != null) {
					break; // If we have a page object, stop searching.
				}
			}
		}
		if(page == null) {
			throw new PageNotFoundException(errorMessage);
		}
		pages.put(pageName, page);
		return page;
	}

}