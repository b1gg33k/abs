package org.abs.consumer.managers;

import org.abs.consumer.entities.Configuration;
import org.apache.log4j.Logger;

import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 2:39 AM
 */
public class ApplicationManager extends BaseManager {
	private static ApplicationManager instance = null;
	private String applicationName = null;
	private String nameSpace = "org::abs";
	private String localhostname;
	private Configuration configuration = null;

	private ApplicationManager(String applicationName) {
		super();
		this.applicationName = applicationName;
		loadConfiguration();
	}

	private ApplicationManager(String applicationName, String nameSpace) {
		super();
		this.applicationName = applicationName;
		this.nameSpace = nameSpace;
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
		String applicationName = "noname";
		if (null != properties){
			applicationName = properties.getProperty("application.name",applicationName);
		}
		return getInstance(applicationName);
	}

	public static ApplicationManager getInstance(String applicationName) {
		if (null == instance){
			instance = new ApplicationManager(applicationName);
		}
		return instance;
	}

	public static ApplicationManager getInstance(String applicationName, String nameSpace) {
		if (null == instance){
			synchronized (instance){
				instance = new ApplicationManager(applicationName,nameSpace);
			}
		}
		return instance;
	}

	private void loadConfiguration(){
		String hostname = getHostname();

	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
}
