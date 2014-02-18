package org.abs.consumer.managers;

import junit.framework.Assert;
import org.abs.consumer.entities.Experiment;
import org.abs.consumer.entities.Group;
import org.abs.consumer.entities.Variant;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.*;

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

	@Test
	public void testSaveLoadEntity() throws Exception {
		List<Group> groups = new ArrayList<Group>();
		groups.add(new Group("groupA"));
		groups.add(new Group("groupB"));

		List<Variant> variants = new ArrayList<Variant>();
		Variant variant1 = new Variant("variant1");
		variant1.setGroups(groups);
		variants.add(variant1);

		Variant variant2 = new Variant("variant2");
		variant2.setGroups(groups);
		variants.add(variant2);

		Experiment experiment = new Experiment("testExperiment", groups, variants);

		StorageManager.getInstance().saveEntity(experiment);
		Experiment resultExperiment = StorageManager.getInstance().loadEntity(experiment.getId(),Experiment.class);

		Assert.assertNotNull(resultExperiment);
		Assert.assertEquals("groupA", resultExperiment.getGroups().get(0).getId());
		Assert.assertEquals("groupB", resultExperiment.getGroups().get(1).getId());
		Assert.assertEquals("variant1", resultExperiment.getVariants().get(0).getId());
		Assert.assertEquals("variant2", resultExperiment.getVariants().get(1).getId());

		Assert.assertEquals("groupA",resultExperiment.getVariants().get(0).getGroups().get(0).getId());
		Assert.assertEquals("groupB",resultExperiment.getVariants().get(0).getGroups().get(1).getId());
		Assert.assertEquals("groupA",resultExperiment.getVariants().get(1).getGroups().get(0).getId());
		Assert.assertEquals("groupB",resultExperiment.getVariants().get(1).getGroups().get(1).getId());
	}
}
