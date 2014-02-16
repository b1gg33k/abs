package org.abs.consumer.managers;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/16/14
 * Time: 2:08 PM
 */
public class BaseManager {
	protected static Logger log = Logger.getLogger(BaseManager.class);;

	protected static Properties properties = null;
	public BaseManager() {
		if (null == properties){
			log = Logger.getLogger(this.getClass());
			properties = new Properties();
			InputStream inputStream = getClass().getResourceAsStream("abs.properties");
			if (null != inputStream){
				try {
					properties.load(inputStream);
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
