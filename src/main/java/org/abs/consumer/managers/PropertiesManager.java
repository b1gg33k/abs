package org.abs.consumer.managers;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/24/14
 * Time: 8:47 PM
 */
public class PropertiesManager {
	protected static Logger log = Logger.getLogger(PersonaManager.class);

	private static PropertiesManager instance = new PropertiesManager();
	protected Properties properties = null;

	private PropertiesManager() {
		log = Logger.getLogger(this.getClass());
		properties = new Properties();
		InputStream inputStream = getClass().getResourceAsStream("/abs.properties");
		if (null != inputStream){
			try {
				properties.load(inputStream);
				if (log.isDebugEnabled()){
					log.debug("Loaded Properties from abs.properties");
					for (Object propName : properties.keySet()){
						log.debug("Property: " + propName + " = " + properties.getProperty(propName.toString()));
					}
				}
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			log.warn("Could not load abs.properties file.");
		}
	}

	public static PropertiesManager getInstance() {
		return instance;
	}

	public static void setInstance(PropertiesManager instance) {
		PropertiesManager.instance = instance;
	}

	public final Properties getProperties() {
		return properties;
	}

	public final String getProperty(String name){
		return properties.getProperty(name);
	}

	public int getIntProperty(String name, int defaultValue){
		int result;
		String propertyString = properties.getProperty(name);
		if (null == propertyString){
			return defaultValue;
		}
		return Integer.parseInt(propertyString);
	}
}
