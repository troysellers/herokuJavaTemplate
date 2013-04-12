package com.force.aus;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppProperties {

	private static final String PROPS_FILE = "application.properties";
	private static Logger LOG;
	
	public static final String DBASE_URL = "dbase.url";
	public static final String WEB_APP_DIR_LOCATION = "web.app.dir.location";
	public static final String PORT = "PORT";
	
	public static Properties appProperties;
	
	/**
	 * Property file should be loaded as the first usage.
	 * This method will attempt to determine if properties are configured by the application.properties 
	 * file or if they are configured as environment variables (Heroku)
	 * 
	 * If environment configuration is detected, application.properties will not be loaded.
	 */
	public static void loadProperties() {
		
		LOG = LoggerFactory.getLogger(AppProperties.class);
		
		if(System.getenv(DBASE_URL) == null) {
			try {
				appProperties = new Properties();
				appProperties.load(AppProperties.class.getClassLoader().getResourceAsStream(PROPS_FILE));
			} catch (Exception e) {
				LOG.error("Caught exception loading properties - throwing runtime exception\n{}",e);
				throw new RuntimeException(e);
			}
		}
		
	}
	
	/**
	 * Method will return String value of Property key.
	 * Depending on configuration, will return either property found in application.properties file
	 * or will return value of propKey from System Envirionment Variables.
	 * 
	 * @param propKey
	 * @return
	 */
	public static String getPropValue(String propKey) {
		if(appProperties != null) 
			return appProperties.getProperty(propKey);
		
		return System.getenv(propKey);
	}

}
