package org.abs.consumer.entities;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/15/14
 * Time: 11:30 PM
 */
public class ConfigurationTest {
	static Logger log = Logger.getLogger(ConfigurationTest.class.getName());
	@Test
	public void test() throws Exception {
		Configuration configuration = new Configuration("test");
		configuration.setExperimentTTL(3600L);
		configuration.setPersonaTTL(3600L);
		String string = configuration.toString();
		Assert.assertNotNull(string);
		log.debug(string);
		String json = configuration.toJson();
		Assert.assertNotNull(json);
		log.debug(json);
	}
}
