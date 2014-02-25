package org.abs.consumer.managers;

import com.sun.org.apache.xerces.internal.impl.PropertyManager;
import org.apache.log4j.Logger;

import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 2:39 AM
 */
public class ApplicationManager {
	protected static Logger log = Logger.getLogger(ApplicationManager.class);

	private static ApplicationManager instance = null;
	private String applicationName = "noname";
	private String baseNamespace = "org::abs";

	public ApplicationManager() {
		super();
		applicationName = PropertiesManager.getInstance().getProperties().getProperty("application.name",applicationName);
		baseNamespace = PropertiesManager.getInstance().getProperties().getProperty("baseNamespace","org::abs");
		loadConfiguration();
	}

	private String getHostname(){
		String hostname = "localhost";
		try {
			hostname = java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			log.error("Error finding hostname, fallback to localhost: " + e.getLocalizedMessage());
			log.debug("Stacktrace", e);
		}
		return hostname;
	}

	public static synchronized ApplicationManager getInstance() {
		if (null == instance){
			instance = new ApplicationManager();
		}
		return instance;
	}

	private void loadConfiguration(){
		String hostname = getHostname();

	}

	public String getBaseNamespace() {
		return baseNamespace;
	}

	public void setBaseNamespace(String baseNamespace) {
		this.baseNamespace = baseNamespace;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
}
