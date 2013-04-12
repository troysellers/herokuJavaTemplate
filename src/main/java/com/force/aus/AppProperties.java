package com.force.aus;

import java.util.Enumeration;
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
	 * Load property file
	 */
	public static void loadProperties() {
		
		LOG = LoggerFactory.getLogger(AppProperties.class);
		
		try {
			appProperties = new Properties();
			appProperties.load(AppProperties.class.getClassLoader().getResourceAsStream(PROPS_FILE));
			Enumeration<Object> en = appProperties.elements();
			while(en.hasMoreElements()) {
				LOG.info("Property [{}]", en.nextElement());
			}
		} catch (Exception e) {
			LOG.error("Caught exception loading properties - throwing runtime exception\n{}",e);
			throw new RuntimeException(e);
		}

		
	}
	
	/**
	 * Will look for environment variable first, then in application.properties
	 * 
	 * @param propKey
	 * @return
	 */
	public static String getPropValue(String propKey) {
		
		String value = System.getenv(propKey);
		if(value != null && !value.isEmpty())
			return value;
		
		return appProperties.getProperty(propKey);
	}

}
