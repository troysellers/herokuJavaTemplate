package com.force.aus;

import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.jetty.nosql.memcached.MemcachedSessionIdManager;
import org.eclipse.jetty.nosql.memcached.MemcachedSessionManager;
import org.eclipse.jetty.nosql.memcached.spymemcached.HerokuSpyMemcachedClientFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	
	private static Logger LOG;
	
	private static String[] configClasses = {
		 "org.eclipse.jetty.webapp.WebInfConfiguration",
		  "org.eclipse.jetty.webapp.WebXmlConfiguration",
		  "org.eclipse.jetty.webapp.MetaInfConfiguration",
		  "org.eclipse.jetty.webapp.FragmentConfiguration",
		  "org.eclipse.jetty.plus.webapp.EnvConfiguration", //add for jndi
		  "org.eclipse.jetty.plus.webapp.PlusConfiguration", // add for jndi
		  "org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
		  "org.eclipse.jetty.webapp.TagLibConfiguration"
	    } ;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		AppProperties.loadProperties();
		LOG = LoggerFactory.getLogger(Main.class);
		dumpClasspath();
		
		String webappDirLocation = AppProperties.getPropValue(AppProperties.WEB_APP_DIR_LOCATION);
		String webPort = AppProperties.getPropValue(AppProperties.PORT);
		if(webPort == null || webPort.isEmpty()) 
			webPort = "8080";
		
		/* UNCOMMENT FOR JNDI 
		System.setProperty("java.naming.factory.url", "org.eclipse.jetty.jndi");
		System.setProperty("java.naming.factory.initial", "org.eclipse.jetty.jndi.InitialContextFactory");
		*/
		
		Server server = new Server(Integer.valueOf(webPort));
		
		/* TODO - handle local and heroku deployment
		 * 
		 * Enable Memcache for using HTTPSession in your app 
		 * This uses an Heroku specific client factory, probably shoud be configured 
		 * to work locally or not
		 */
		/* UNCOMMENT FOR MEMCACHE SESSION HANDLING
		MemcachedSessionIdManager idManager = new MemcachedSessionIdManager(server);
		idManager.setClientFactory(new HerokuSpyMemcachedClientFactory());
		server.setSessionIdManager(idManager);
		server.setAttribute("memcachedSessionIdManager", idManager);
		
		MemcachedSessionManager sessionManager = new MemcachedSessionManager();
		sessionManager.setSessionIdManager(idManager);
		*/
		WebAppContext context = new WebAppContext();
		context.setConfigurationClasses(configClasses);
		context.setContextPath("/");
		context.setDescriptor(webappDirLocation+"/WEB-INF/web.xml");
		context.setResourceBase(webappDirLocation);
		/* Uncomment for MemCache Session handling */
		//context.setSessionHandler(new SessionHandler(sessionManager));
		/* Uncomment and name the Data source */
		//context.setAttribute("DataSourceNAme", getJNDIResource());
		
        //Parent loader priority is a class loader setting that Jetty accepts.
        //By default Jetty will behave like most web containers in that it will
        //allow your application to replace non-server libraries that are part of the
        //container. Setting parent loader priority to true changes this behavior.
        //Read more here: http://wiki.eclipse.org/Jetty/Reference/Jetty_Classloading
        context.setParentLoaderPriority(true);

        server.setHandler(context);
        server.start();
        server.join();   		
		
	}
	
	
	/*
	 * Configure JNDI Resource for using Hibernate or other ORM mapping tools
	 * This on configures a Postgres data source and makes it available to the application.
	 */
/*
    private static Resource getJNDIResource() throws URISyntaxException, NamingException {
    	
    	// database URL should be postgres://username:password@host:port/schema
    	
    	String databaseURL = System.getenv("DATABASE_URL") ;
    	if(databaseURL == null) {
    		databaseURL = AppProperties.getPropValue(AppProperties.DBASE_URL);
    	}
    	
     	URI dbUri = new URI(databaseURL);
    	String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        
        LOG.info("DBURI ["+dbUri+"]");
        LOG.info("Username ["+username+"]");
        LOG.info("Password ["+password+"]");
        LOG.info("Host ["+dbUri.getHost()+"]");
        LOG.info("Port ["+dbUri.getPort()+"]");
        LOG.info("Path ["+dbUri.getPath()+"]");
        
        PGSimpleDataSource pgDS = new PGSimpleDataSource();
        pgDS.setDatabaseName(dbUri.getPath().replaceAll("/", ""));
        pgDS.setUser(username);
        pgDS.setPassword(password);
        pgDS.setServerName(dbUri.getHost());
        pgDS.setPortNumber(dbUri.getPort());
        
        return new Resource("jdbc/obmDS", pgDS);
        
    }
*/
    /*
     * Utility for dumping classpath information if required.
     * 
     */
    private static void dumpClasspath() {
    	ClassLoader cl = ClassLoader.getSystemClassLoader();
    	LOG.info("ClassPath -------");
    	for(URL url : ((URLClassLoader)cl).getURLs()) {
    		LOG.info(url.toExternalForm());
    	}
    	LOG.info("------CLASSPATH END------");
    }	
	
}
