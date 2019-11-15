package com.dougnoel.sentinel.pages;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.exceptions.ConfigurationMappingException;
import com.dougnoel.sentinel.exceptions.ConfigurationNotFound;
import com.dougnoel.sentinel.exceptions.ConfigurationParseException;
import com.dougnoel.sentinel.exceptions.FileNotFoundException;
import com.dougnoel.sentinel.exceptions.IOException;
import com.dougnoel.sentinel.exceptions.MissingConfigurationException;
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
	
	/**
	 * Returns a page object if it exists in the package searched.
	 * @param pageName String the name of the page object class to instantiate
	 * @param packageName String the name of the package to search
	 * @return Page the page object if it exists, otherwise null
	 */
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
     * @throws MissingConfigurationException if the requested configuration property has not been set
     * @throws ConfigurationParseException if error thrown while reading configuration file into sentinel
     * @throws ConfigurationMappingException if error thrown while mapping configuration file to sentinel
     * @throws IOException if other error occurs when mapping yml file into sentinel 
	 * @throws FileNotFoundException if the sentinel configuration file does not exist
	 * @throws PageNotFoundException if page could not be built or retrieved.
	 * @throws ConfigurationNotFound if the value is not found in the configuration file
	 */
	public static Page buildOrRetrievePage(String pageName) throws ConfigurationParseException, ConfigurationMappingException, IOException, MissingConfigurationException, FileNotFoundException, PageNotFoundException, ConfigurationNotFound {
		Page page = pages.get(pageName);
		if (page != null) {
			return page;
		} else {
			if (pageObjectPackagesList == null) {
				pageObjectPackagesList = ConfigurationManager.getPageObjectPackageList();
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
			throw new PageNotFoundException("The page you want to test could not be built. At least one Page object package is required to run a test. Please add a pageObjectPackage property to your conf/sentinel.yml configuration file and try again.");
		}
		pages.put(pageName, page);
		return page;
	}

}