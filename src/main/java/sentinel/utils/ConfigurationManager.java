package sentinel.utils;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import sentinel.exceptions.AccessDeniedException;
import sentinel.exceptions.ConfigurationMappingException;
import sentinel.exceptions.ConfigurationParseException;
import sentinel.exceptions.FileNotFoundException;
import sentinel.exceptions.IOException;
import sentinel.exceptions.MissingConfigurationException;
import sentinel.exceptions.PageNotFoundException;
import sentinel.exceptions.URLNotFoundException;
import sentinel.pages.PageData;
import sentinel.pages.PageManager;
import sentinel.utils.ConfigurationData;

/**
 *  Manages configuration actions, changes, and interaction with PageObject including functionality to get default timeout,
 *  default time units, environment data, config path and package lists from PageObject, current URL or URL from a given pageName,
 *  user name or password from given account, env, or page, loads pageData for a given pageName, authentication with SSL, 
 *  searching directories, and class getter and setter method.
 *  
 */
public class ConfigurationManager {
	private static final Logger log = LogManager.getLogger(ConfigurationManager.class); // Create a logger.

	private static ConfigurationManager instance = null;
	
	private static Properties appProps = new Properties();
	
	private static String downloadDirectory = "../../Downloads";
	/* default timeout in seconds */
	private static long timeout = 10L;
	
	private static ConfigurationData sentinelConfigurations = null;
	
	/**
	 * Name of the package where page objects are stored. If you change the name,
	 * you need to change this value. To search in additional locations, you can set
	 * a system property.
	 *
	 * <b>Example:</b>
	 * <p>
	 * <code>System.setProperty("pageObjectPackages", "pages.SharedComponent,pages.UserAdminTool");</code>
	 */
	private static String defaultPackageName = "pages,apis";

	protected ConfigurationManager() {
		// Exists only to defeat instantiation.
	}
	/**
	 * Returns instance of ConfigurationManager
	 * 
	 * @return ConfigurationManager the instance of this class
	 */
	public static ConfigurationManager getInstance() {
		if (instance == null) {
			instance = new ConfigurationManager();
		}
		return instance;
	}
	/**
	 * Getter function for the downloadDirectory
	 * 
	 * @return String the download directory
	 */
	public static String getDownloadDirectory() {
		return downloadDirectory;
	}
	
	/**
	 * Sets download directory with the given directory path
	 * 
	 * @param newDirectory String the new directory path
	 */
	public static void setDownloadDirectory(String newDirectory) {
		downloadDirectory = newDirectory;
	}
	
	/**
	 * Returns the configuration value for the given configuration property and the given environment from the ConfigurationData class.
	 * 
	 * @param configurationKey String the key for the requested configuration property
	 * @return String the configuration value
	 * @throws ConfigurationParseException if an exception occurs when parsing configuration to data object
	 * @throws ConfigurationMappingException if an exception is thrown when mapping yml configurations to data object
	 * @throws IOException if an exception is thrown while mapping ConfigurationData class to new ConfigurationData object
	 * @throws MissingConfigurationException if system environment is not set, will prompt user to set environment
	 * @throws FileNotFoundException if the sentinel configuration file does not exist.
	 */
	public static String getOrCreateConfigurationData(String configurationKey) throws ConfigurationParseException, ConfigurationMappingException, MissingConfigurationException, FileNotFoundException, IOException {	
		//First we see if the property is set on the maven commandline or in code.
		String data = System.getProperty(configurationKey);
		if (data != null) {
			return data;
		}
		
		if(sentinelConfigurations == null) {
			try {
				ObjectMapper mapper = new ObjectMapper(new YAMLFactory()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				sentinelConfigurations = mapper.readValue( new ConfigurationData(), ConfigurationData.class );
			} catch (JsonParseException e) {
				String errorMessage = StringUtils.format("Configuration file {} is not a valid YAML file. Could not load the {} property. Please fix the file or pass the property in on the commandline using the -D option.", sentinelConfigurations.getAbsolutePath(), configurationKey);
				log.error(errorMessage);
				throw new ConfigurationParseException(errorMessage, e);
			} catch (JsonMappingException e) {
				String errorMessage = StringUtils.format("Configuration file {} has incorrect formatting and cannot be read. Could not load the {} property. Please fix the file or pass the property in on the commandline using the -D option.", sentinelConfigurations.getAbsolutePath(), configurationKey);
				log.error(errorMessage);
				throw new ConfigurationMappingException(errorMessage, e);
			} catch (java.io.FileNotFoundException e) {
				String errorMessage = StringUtils.format("Configuration file {} cannot be found in the specified location. Could not load the {} property. Please fix the file or pass the property in on the commandline using the -D option.", "conf/sentinel.yml", configurationKey);
				log.error(errorMessage);
				throw new FileNotFoundException(errorMessage, e);
			} catch (java.io.IOException e) {
				String errorMessage = StringUtils.format("Configuration file {} cannot be opened in the specified location. Could not load the {} property. Please fix the file read properties or pass the property in on the commandline using the -D option.", "conf/sentinel.yml", configurationKey);
				log.error(errorMessage);
				throw new IOException(errorMessage, e);
			}
		}
	 
		return sentinelConfigurations.getConfigurationValue(getEnvironment(), configurationKey);	
	}
	
	/**
	 * Returns the configuration property for the given key from the helper function which interfaces with the
	 * ConfigurationData object if it exists. If it does not exist, it is created by the helper function.
	 * 
	 * @see ConfigurationManager#getOrCreateConfigurationData(String)
	 * @param property String the requested configuration property key
	 * @return String the configuration property value
	 * @throws ConfigurationParseException if an exception occurs when parsing configuration to data object
	 * @throws ConfigurationMappingException if an exception is thrown when mapping yml configurations to data object
	 * @throws IOException if an exception is thrown while mapping ConfigurationData class to new ConfigurationData object
	 * @throws MissingConfigurationException if system environment is not set, will prompt user to set environment
	 * @throws FileNotFoundException if the sentinel configuration file does not exist.
	 */
	public static String getProperty(String property) throws ConfigurationParseException, ConfigurationMappingException, IOException, MissingConfigurationException, FileNotFoundException {
		String systemProperty = System.getProperty(property);
		if(systemProperty == null) {
			systemProperty = getOrCreateConfigurationData(property);
		}		
		return systemProperty;
	}
	
	/**
	 * Returns the name of all the folders to be searched for page objects.
	 * 
	 * @return String[] the list of page object folders
	 * @throws ConfigurationParseException if an exception occurs when parsing configuration to data object
	 * @throws ConfigurationMappingException if an exception is thrown when mapping yml configurations to data object
	 * @throws IOException if an exception is thrown while mapping ConfigurationData class to new ConfigurationData object
	 * @throws MissingConfigurationException if system environment is not set, will prompt user to set environment
	 * @throws FileNotFoundException if the sentinel configuration file does not exist.
	 */
	public static String[] getPageObjectPackageList() throws ConfigurationParseException, ConfigurationMappingException, IOException, MissingConfigurationException, FileNotFoundException {
		String pageObjectPackages = getOrCreateConfigurationData("pageObjectPackages");
		pageObjectPackages = (pageObjectPackages == null) ? defaultPackageName
				: pageObjectPackages + "," + defaultPackageName;
		log.trace("pageObjectPackages: {}", pageObjectPackages);
		return StringUtils.split(pageObjectPackages, ',');
	}

	/**
	 * Returns the system environment, returns an exception if no env if found, forcing the user to set the env.
	 * 
	 * @return String text of system env info
	 * @throws MissingConfigurationException if no env variable has been set
	 */
	public static String getEnvironment() throws MissingConfigurationException {
		String env = System.getProperty("env");
		if (env == null)
			throw new MissingConfigurationException("Enviroment is not set, please restart your test and pass -Denv=\"<your environment>\"");
		return env;
	}

	/**
	 * Returns and Sets the trust level for SSL certificates based on the "ssltrust" system
	 * property, and defaults to none if no value is set.
	 * 
	 * @return String the SSL trust level set
	 * @throws ConfigurationParseException if an exception occurs when parsing configuration to data object
	 * @throws ConfigurationMappingException if an exception is thrown when mapping yml configurations to data object
	 * @throws IOException if an exception is thrown while mapping ConfigurationData class to new ConfigurationData object
	 * @throws MissingConfigurationException if system environment is not set, will prompt user to set environment
	 * @throws FileNotFoundException if the sentinel configuration file does not exist.
	 */
//	public static String setSSLTrustLevel() throws ConfigurationParseException, ConfigurationMappingException, IOException, MissingConfigurationException, FileNotFoundException {
//		String trustLevel = getOrCreateConfigurationData("ssltrust");
//		if (trustLevel == null) {
//			trustLevel = "none";
//		}
//		switch (trustLevel) {
//		case K.ALL:
//			SSLUtilities.trustAllHttpsCertificates();
//			SSLUtilities.trustAllHostnames();
//			break;
//		case K.CERTS:
//			SSLUtilities.trustAllHttpsCertificates();
//			break;
//		case K.HOSTS:
//			SSLUtilities.trustAllHostnames();
//			break;
//		default:
//			break;
//		}
//		return trustLevel;
//	}

	/**
	 * Returns the YAML config file path in the project for a given page object.
	 * 
	 * @param pageName String the name of the page object
	 * @return File the OS path to the config file
	 * @throws FileNotFoundException if the config file is not found in the project
	 * @throws AccessDeniedException if the project is not readable
	 */
	public static File getPageObjectConfigPath(String pageName) throws FileNotFoundException, AccessDeniedException {
		String filename = pageName + ".yml";
		File result = searchDirectory(new File("src/"), filename);

		if (result == null) {
			String errorMessage = StringUtils.format("Failed to locate the {} configuration file. Please ensure the file exists in the same directory as the page object.", filename);
			log.error(errorMessage);
			throw new FileNotFoundException(filename);
		}

		return result;
	}

	/**
	 * Returns a File handler to a file if it is found in the given directory or any
	 * sub-directories.
	 * 
	 * @param directory File the directory to start the search
	 * @param fileName String the full name of the file with extension to find
	 * @return File the file that is found, null if nothing is found
	 * @throws AccessDeniedException when a directory or file cannot be read.
	 */
	public static File searchDirectory(File directory, String fileName) throws AccessDeniedException {
		log.trace("Searching directory {}", directory.getAbsoluteFile());
		File searchResult = null;
		if (directory.canRead()) {
			for (File temp : directory.listFiles()) {
				if (temp.isDirectory()) {
					searchResult = searchDirectory(temp, fileName);
				} else {
					if (fileName.equals(temp.getName()))
						searchResult = temp.getAbsoluteFile();
				}
				if (searchResult != null) {
					break;
				}
			}
		} else {
			throw new AccessDeniedException(directory.getAbsoluteFile().toString());
		}
		return searchResult;
	}
  
	/**
	 * Returns page data through yaml instructions to a config path in given pageName string. 
	 * 
	 * @see sentinel.utils.ConfigurationManager#getPageObjectConfigPath(String)
	 * @param pageName String the name of the page for which the data is retrieved
	 * @return PageData the class for the data on desired page
	 * @throws IOException if no data is returned
	 * @throws ConfigurationParseException if error occurs when reading configuration file
	 * @throws ConfigurationMappingException if error occurs when mapping configuration values to sentinel
	 * @throws FileNotFoundException if the sentinel configuration file does not exist
	 * @throws AccessDeniedException if the sentinel configuration file cannot be read
	 */
	protected static PageData loadPageData(String pageName) throws ConfigurationParseException, ConfigurationMappingException, FileNotFoundException, IOException, AccessDeniedException {
		PageData pageData = null;
		try {
			pageData = PageData.loadYaml(getPageObjectConfigPath(pageName));
		} catch (java.nio.file.AccessDeniedException e) {
			String errorMessage = StringUtils.format("Could not access the file {}.yml. Please ensure the file can be read by the current user and is not password protected.", pageName);
			log.error(errorMessage);
			throw new AccessDeniedException(errorMessage, e);
		} catch (java.io.IOException e) {
			String errorMessage = StringUtils.format("Could not access the file {}.yml. Please ensure the file exists and the the pageObjectPackages value is set to include its package.", pageName);
			log.error(errorMessage);
			throw new IOException(errorMessage, e);
		}
		if (pageData == null) {
			throw new IOException("No data was loaded.");
		}
		log.trace("Page data loaded: {}", pageName);
		return pageData;
	}

	/**
	 * Returns the URL for the currently active page based on the environment value set. 
	 * 
	 * @see sentinel.pages.PageManager#getPage()
	 * @see sentinel.pages.Page#getName()
	 * @return String the desired URL
	 * @throws IOException from sentinel.utils.ConfigurationManager#loadPageData(String)
	 * @throws PageNotFoundException if page is not found
	 * @throws URLNotFoundException if url is not found for the page
	 * @throws ConfigurationParseException if error occurs when reading configuration file
	 * @throws ConfigurationMappingException if error occurs when mapping configuration values to sentinel
	 * @throws FileNotFoundException if the sentinel configuration file does not exist
	 * @throws AccessDeniedException if the sentinel configuration file cannot be read
	 * @throws MissingConfigurationException if the requested configuration property has not been set
	 */
	public static String getUrl() throws IOException, ConfigurationParseException, ConfigurationMappingException, FileNotFoundException, AccessDeniedException, URLNotFoundException, PageNotFoundException, MissingConfigurationException {
		return getUrl(PageManager.getPage().getName());
	}
	
	/**
	 * Returns a URL for the given page name based on the environment value set.
	 
	 * @param pageName String the name of the page from which the url is retrieved
	 * @return String baseUrl the url for the given page and current environment
	 * @throws IOException from sentinel.utils.ConfigurationManager#loadPageData(String)
	 * @throws URLNotFoundException if page config yml file does not have a url map
	 * @throws ConfigurationParseException if error occurs when reading configuration file
	 * @throws ConfigurationMappingException if error occurs when mapping configuration values to sentinel
	 * @throws FileNotFoundException if the sentinel configuration file does not exist
	 * @throws AccessDeniedException if the sentinel configuration file cannot be read
	 * @throws MissingConfigurationException if the requested configuration property has not been set
	 */
	public static String getUrl(String pageName) throws IOException, ConfigurationParseException, ConfigurationMappingException, FileNotFoundException, AccessDeniedException, URLNotFoundException, MissingConfigurationException {
		String baseURL = null;
		PageData pageData = loadPageData(pageName);
		// Get the test environment. If none is passed, it defaults to dev. See the
		// Readme.md file for how to pass the env
		// on the command line.
		String env = System.getProperty("env");
		if (env == null)
			env = "dev";

		try {
			if (pageData.urls.containsKey(env)) {
				baseURL = pageData.urls.get(env);
			} else {
				baseURL = pageData.urls.get("base");
				baseURL = StringUtils.replace(baseURL, "{env}", env);
			}
		} catch (NullPointerException e){
			String errorMessage = StringUtils.format("A url was not found for the {} environment in your page's yml file. Please add a URL to the yml file. See the project README for details.", getEnvironment());
			log.error(errorMessage);
			throw new URLNotFoundException(errorMessage, e);
			
		}
		return baseURL;
	}
  
	/**
	 * Returns the current Usernane
	 * 
	 * @see sentinel.utils.ConfigurationManager#getUsernameOrPassword(String)
	 * @return String the username
	 * @throws IOException from sentinel.utils.ConfigurationManager#loadPageData(String)
	 * @throws ConfigurationParseException if error occurs when reading configuration file
	 * @throws ConfigurationMappingException if error occurs when mapping configuration values to sentinel
	 */
	public static String getUsername() throws IOException, ConfigurationParseException, ConfigurationMappingException, Exception {
		return getUsernameOrPassword("username");
	}
	/**
	 * Returns the current password
	 * 
	 * @see sentinel.utils.ConfigurationManager#getUsernameOrPassword(String)
	 * @return String password
	 * @throws IOException from sentinel.utils.ConfigurationManager#loadPageData(String)
	 * @throws ConfigurationParseException if error occurs when reading configuration file
	 * @throws ConfigurationMappingException if error occurs when mapping configuration values to sentinel
	 */
	public static String getPassword() throws IOException, ConfigurationParseException, ConfigurationMappingException, Exception {
		return getUsernameOrPassword("password");
	}

	/**
	 * Returns given username or password key from the pageData env method
	 * 
	 * @param key String the needed item, either username or password
	 * @return String the given item value
	 * @throws IOException from sentinel.utils.ConfigurationManager#loadPageData(String)
	 * @throws MissingConfigurationException if env is not set
	 * @throws PageNotFoundException if no page is found
	 * @throws ConfigurationParseException if error occurs when reading configuration file
	 * @throws ConfigurationMappingException if error occurs when mapping configuration values to sentinel
	 * @throws AccessDeniedException if the sentinel configuration file cannot be read
	 * @throws FileNotFoundException if the sentinel configuration file does not exist
	 */
	public static String getUsernameOrPassword(String key) throws IOException, MissingConfigurationException, ConfigurationParseException, ConfigurationMappingException, PageNotFoundException, FileNotFoundException, AccessDeniedException {
		String env = getEnvironment();
		return getUsernameOrPassword(PageManager.getPage().getName(), env, key);
	}

	/**
	 * Returns username or password for a given page env from current PageData account
	 * 
	 * @param pageName String name of needed page
	 * @param env String user environment
	 * @param key String user name or password
	 * @return String requested username or password
	 * @throws IOException from sentinel.utils.ConfigurationManager#loadPageData(String)
	 * @throws ConfigurationParseException if error occurs when reading configuration file
	 * @throws ConfigurationMappingException if error occurs when mapping configuration values to sentinel
	 * @throws AccessDeniedException if the sentinel configuration file cannot be read
	 * @throws FileNotFoundException if the sentinel configuration file does not exist
	 */
	public static String getUsernameOrPassword(String pageName, String env, String key) throws IOException, ConfigurationParseException, ConfigurationMappingException, FileNotFoundException, AccessDeniedException{
		PageData pageData = loadPageData(pageName);
		log.debug(pageData.getAccount(env).get(key));
		String data = pageData.getAccount(env).get(key);
		log.debug(data);
		return data;
	}

	/**
	 * Returns the username for the given account and the currently set environment.
	 * @param account String the name of the account as stored in the configuration file for the current envrionment
	 * @return String requested username value
	 * @throws IOException from sentinel.utils.ConfigurationManager#loadPageData(String)
	 * @throws ConfigurationParseException if error occurs when reading configuration file
	 * @throws ConfigurationMappingException if error occurs when mapping configuration values to sentinel
	 * @throws AccessDeniedException if the sentinel configuration file cannot be read
	 * @throws FileNotFoundException if the sentinel configuration file does not exist
	 * @throws PageNotFoundException if no page is found
	 * @throws MissingConfigurationException if the requested configuration property has not been set
	 */
	public static String getUsername(String account) throws IOException, ConfigurationParseException, ConfigurationMappingException, MissingConfigurationException, PageNotFoundException, FileNotFoundException, AccessDeniedException {
		return getUsernameOrPassword(account, "username");
	}
	
	/**
	 * Returns password for given account
	 * @param account String user account
	 * @return String requested password
	 * @throws IOException from sentinel.utils.ConfigurationManager#loadPageData(String)
	 * @throws ConfigurationParseException if error occurs when reading configuration file
	 * @throws ConfigurationMappingException if error occurs when mapping configuration values to sentinel
	 * @throws AccessDeniedException if the sentinel configuration file cannot be read
	 * @throws FileNotFoundException if the sentinel configuration file does not exist
	 * @throws PageNotFoundException if no page is found
	 * @throws MissingConfigurationException if the requested configuration property has not been set
	 */
	public static String getPassword(String account) throws IOException, ConfigurationParseException, ConfigurationMappingException, MissingConfigurationException, PageNotFoundException, FileNotFoundException, AccessDeniedException {
		return getUsernameOrPassword(account, "password");
	}
	
	/**
	 * Returns username or password. Takes a given account and env
	 * @param account String user account
	 * @param key String username or password
	 * @return String requested username or password
	 * @throws IOException from sentinel.utils.ConfigurationManager#loadPageData(String)
	 * @throws MissingConfigurationException if env is not set
	 * @throws PageNotFoundException if no page is found
	 * @throws ConfigurationParseException if error occurs when reading configuration file
	 * @throws ConfigurationMappingException if error occurs when mapping configuration values to sentinel
	 * @throws AccessDeniedException if the sentinel configuration file cannot be read
	 * @throws FileNotFoundException sentinel configuration file does not exist
	 */

	public static String getUsernameOrPassword(String account, String key) throws IOException, MissingConfigurationException, ConfigurationParseException, ConfigurationMappingException, PageNotFoundException, FileNotFoundException, AccessDeniedException {
		String env = getEnvironment();
		return getUsernameOrPassword(PageManager.getPage().getName(), env, account, key);
	}
	/**
	 * Returns username or password. Parent Method.
	 * 
	 * Creates pageData object and gets user name or password from its account and env. Logs the key from account info.
	 * and logs data var before returning.
	 * 
	 * @param pageName String the name of current browser page
	 * @param env String user environment
	 * @param account String user account
	 * @param key String username or password
	 * @return String requested username or password
	 * @throws IOException from sentinel.utils.ConfigurationManager#loadPageData(String)
	 * @throws ConfigurationParseException if error occurs when reading configuration file
	 * @throws ConfigurationMappingException if error occurs when mapping configuration values to sentinel
	 * @throws FileNotFoundException if the sentinel configuration file does not exist
	 * @throws AccessDeniedException if the sentinel configuration file cannot be read
	 */
	public static String getUsernameOrPassword(String pageName, String env, String account, String key)
			throws IOException, ConfigurationParseException, ConfigurationMappingException, FileNotFoundException, AccessDeniedException {
		PageData pageData = loadPageData(pageName);
		log.debug(pageData.getAccount(env, account).get(key));
		String data = pageData.getAccount(env, account).get(key);
		log.debug(data);
		return data;
	}
	
	/**
	 * Returns configuration data for a specific key and environment.
	 * @param testData String the test data name in the configuration file
	 * @param key String the key under which the data is stored
	 * @return String requested value
	 * @throws ConfigurationParseException if error occurs when reading configuration file
	 * @throws ConfigurationMappingException if error occurs when mapping configuration values to sentinel
	 * @throws PageNotFoundException if no page is found
	 * @throws AccessDeniedException if the sentinel configuration file cannot be read
	 * @throws FileNotFoundException if the sentinel configuration file does not exist
	 * @throws MissingConfigurationException if env is not set
	 * @throws IOException from sentinel.utils.ConfigurationManager#getTestData(String, String, String)
	 */
	public static String getTestData(String testData, String key) throws ConfigurationParseException, ConfigurationMappingException, IOException, MissingConfigurationException, FileNotFoundException, AccessDeniedException, PageNotFoundException {
		
		return getTestData(PageManager.getPage().getName(), testData, key);
	}
	
	/**
	 * Returns configuration data for a specific key and env
	 * @param pageName String the name of current browser page
	 * @param testData String is test data from configuration
	 * @param key String is a key of the test data from configuration
	 * @return String requested key value
	 * @throws IOException if the configuration file cannot be read
     * @throws ConfigurationParseException if the configuration file cannot be parsed
     * @throws ConfigurationMappingException if the configuration file is formatted incorrectly
	 * @throws MissingConfigurationException if the requested configuration property has not been set
	 * @throws FileNotFoundException if the sentinel configuration file does not exist.
	 * @throws AccessDeniedException if the sentinel configuration file cannot be read.
	 */
	public static String getTestData(String pageName, String testData, String key)
			throws IOException, ConfigurationParseException, ConfigurationMappingException, MissingConfigurationException, FileNotFoundException, AccessDeniedException {
		String env = getEnvironment();
		PageData pageData = loadPageData(pageName);
		log.debug(pageData.getTestData(env, testData).get(key));
		String data = pageData.getTestData(env, testData).get(key);
		log.debug(data);
		return data;
	}

	/**
	 * Returns the default value set in the timeout property.
	 * The default if the property is not set is 10.
	 * The method getDefaultTimeUnit is used to determine how the value is measured.
	 * @return long the timeout 
	 * @throws MissingConfigurationException if the requested configuration property has not been set
     * @throws ConfigurationParseException if error thrown while reading configuration file into sentinel
     * @throws ConfigurationMappingException if error thrown while mapping configuration file to sentinel
     * @throws IOException if other error occurs when mapping yml file into sentinel
	 * @throws FileNotFoundException if the sentinel configuration file does not exist.
	 */
	public static long getDefaultTimeout() throws ConfigurationParseException, ConfigurationMappingException, IOException, MissingConfigurationException, FileNotFoundException {
		String timeoutProp = getOrCreateConfigurationData("timeout");
		long timeout;
		if(timeoutProp == null) {
			timeout = ConfigurationManager.timeout;
		} else {
			timeout = Long.parseLong(timeoutProp);
		}
		return timeout;
	}

	/**
	 * Returns the timeunit property if it is set for implicit waits, otherwise returns the default.
	 * Possible return values: DAYS  HOURS, MINUTES, SECONDS, MICROSECONDS, MILLISECONDS, NANOSECONDS
	 * The default if the value is not set is TimeUnit.SECONDS.
	 * The method getDefaultTimeout is used to determine the duration of the timeout.
	 * 
	 * @return java.util.concurrent.TimeUnit the default value
	 * @throws MissingConfigurationException if the requested configuration property has not been set
     * @throws ConfigurationParseException if error thrown while reading configuration file into sentinel
     * @throws ConfigurationMappingException if error thrown while mapping configuration file to sentinel
     * @throws IOException if other error occurs when mapping yml file into sentinel
	 * @throws FileNotFoundException if the sentinel configuration file does not exist.
	 */
	public static TimeUnit getDefaultTimeUnit() throws ConfigurationParseException, ConfigurationMappingException, IOException, MissingConfigurationException, FileNotFoundException {
		String unit = StringUtils.capitalize(getOrCreateConfigurationData("timeunit"));
		if(unit == null) {
			return TimeUnit.SECONDS;
		}
		switch (unit) {
		case "DAYS":
			return TimeUnit.DAYS;
		case "HOURS":
			return TimeUnit.HOURS;
		case "MINUTES":
			return TimeUnit.MINUTES;
		case "SECONDS":
			return TimeUnit.SECONDS;
		case "MICROSECONDS":
			return TimeUnit.MICROSECONDS;
		case "MILLISECONDS":
			return TimeUnit.MILLISECONDS;
		case "NANOSECONDS":
			return TimeUnit.NANOSECONDS;
		default:
			return TimeUnit.SECONDS;
		}
	}

	/**
	 * Stores values in a property object for quick and dirty dependency injection.
	 * Replaces space chars with '_' char, makes key all lowercase, and logs action.
	 * 
	 * @param key String the key to set
	 * @param value String the value to set
	 */
	public static void setValue(String key, String value) {
		key = key.replaceAll("\\s+", "_").toLowerCase();
		appProps.setProperty(key, value);
		log.trace("Stored key/value pair: " + key + "/" + value);
	}

	/**
	 * Retrieves values between steps during quick and dirty dependency injection.
	 * Replaces space chars with '_' char, makes key all lowercase, and logs action.
	 *
	 * @param key String the item to get 
	 * @return String the value for the given key
	 */
	public static String getValue(String key) {
		key = key.replaceAll("\\s+", "_").toLowerCase();
		String value = appProps.getProperty(key);
		log.trace("Retrieved key/value pair: " + key + "/" + value);
		return value;
	}
}
