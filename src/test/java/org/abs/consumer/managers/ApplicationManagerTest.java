package org.abs.consumer.managers;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/16/14
 * Time: 2:53 PM
 */
public class ApplicationManagerTest {
	@Test
	public void testGetNameSpace() throws Exception {
		ApplicationManager applicationManager = ApplicationManager.getInstance();
		Assert.assertNotNull(applicationManager.getBaseNamespace());
	}

	@Test
	public void testGetApplicationName() throws Exception {
		ApplicationManager applicationManager = ApplicationManager.getInstance();
		Assert.assertNotNull(applicationManager.getApplicationName());
	}

	@Test
	public void testLoadingProperties() throws Exception {
		ApplicationManager applicationManager = ApplicationManager.getInstance();
		Assert.assertEquals("testapplication", applicationManager.getApplicationName());
	}
}
