package org.abs.consumer.managers;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/15/14
 * Time: 1:27 PM
 */
public class StorageManagerTest {

	@Test
	public void testSaveMap() throws Exception {
		Map<String,String> testMap = new HashMap<String, String>();
		testMap.put("1", "one");
		testMap.put("2", "two");
		testMap.put("3", "three");
		testMap.put("4", "four");

		StorageManager storageManager = StorageManager.getInstance();
		storageManager.saveMap("testSaveMap", testMap);
		Map<String,String> resultsMap = storageManager.loadMap("testSaveMap");

		for (String key : testMap.keySet()){
			Assert.assertTrue(resultsMap.containsKey(key));
			Assert.assertEquals(testMap.get(key),resultsMap.get(key));
		}

	}

	@Test
	public void testSaveList() throws Exception {
		List<String> testList = new ArrayList<String>();
		testList.add("One");
		testList.add("two");
		testList.add("three");
		testList.add("four");

		StorageManager storageManager = StorageManager.getInstance();
		storageManager.saveList("testSaveList",testList);
		List<String> resultsList = storageManager.loadList("testSaveList");

		Assert.assertTrue(resultsList.containsAll(testList));
	}
}
