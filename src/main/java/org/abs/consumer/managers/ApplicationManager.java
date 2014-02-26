package org.abs.consumer.managers;

import org.abs.consumer.entities.Configuration;

import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 2:39 AM
 */
public class ApplicationManager extends BaseManager {
	private static ApplicationManager instance = null;
	private String applicationName = "noname";
	private String baseNamespace = "org::abs";
	private Configuration configuration = null;

	public ApplicationManager() {
		super();
		if (null != properties){
			applicationName = properties.getProperty("application.name",applicationName);
			baseNamespace = properties.getProperty("baseNamespace","org::abs");
		}
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
