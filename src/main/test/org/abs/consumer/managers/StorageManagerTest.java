package org.abs.consumer.managers;

import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/15/14
 * Time: 1:27 PM
 */
public class StorageManagerTest {
	@Test
	public void testLoadMap() throws Exception {

	}

	@Test
	public void testLoadList() throws Exception {

	}

	@Test
	public void testSaveMap() throws Exception {
		StorageManager storageManager = StorageManager.getInstance();
		Map<String,String> data = new HashMap<String, String>();
		data.put("1","one");
		data.put("2","two");
		data.put("3","three");
		data.put("4","four");

		storageManager.saveMap("testSaveMap", data);
		Map<String,String> resultsMap = storageManager.loadMap("testSaveMap");

		for (String key : data.keySet()){
			Assert.assertTrue(resultsMap.containsKey(key));
			Assert.assertEquals(data.get(key),resultsMap.get(key));
		}

	}

	@Test
	public void testSaveList() throws Exception {

	}
}
