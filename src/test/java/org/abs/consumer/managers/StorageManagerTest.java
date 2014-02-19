package org.abs.consumer.managers;

import junit.framework.Assert;
import org.abs.consumer.entities.*;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/15/14
 * Time: 1:27 PM
 */
public class StorageManagerTest {
	@Before
	public void setUp() throws Exception {
		Jedis jedis = StorageManager.getInstance().getPool().getResource();
		jedis.flushDB();
		StorageManager.getInstance().getPool().returnResource(jedis);
	}

	@Test
	public void testSaveMap() throws Exception {
		Map<String,IEntity> testMap = new HashMap<String, IEntity>();
		testMap.put("1", new TestEntity("one"));
		testMap.put("2", new TestEntity("two"));
		testMap.put("3", new TestEntity("three"));
		testMap.put("4", new TestEntity("four"));

		StorageManager storageManager = StorageManager.getInstance();
		storageManager.saveEntityMap(testMap);
		Map<String,TestEntity> resultsMap = storageManager.loadEntityMap(TestEntity.class);

		for (String key : testMap.keySet()){
			Assert.assertTrue(resultsMap.containsKey(key));
			Assert.assertEquals(testMap.get(key).getId(),resultsMap.get(key).getId());
		}
		Assert.assertNotNull("john");

	}

	@Test
	public void testSaveList() throws Exception {
		List<IEntity> testList = new ArrayList<IEntity>();
		testList.add(new TestEntity("One"));
		testList.add(new TestEntity("two"));
		testList.add(new TestEntity("three"));
		testList.add(new TestEntity("four"));

		StorageManager storageManager = StorageManager.getInstance();
		storageManager.saveEntityList(testList);
		List<TestEntity> resultsList = storageManager.loadEntityList(TestEntity.class);
		Map<String,IEntity> resultsMap = new HashMap<String, IEntity>();
		for (IEntity entity : resultsList){
			resultsMap.put(entity.getId(), entity);
		}

		for (IEntity testEntity : testList){
			IEntity entity = resultsMap.get(testEntity.getId());
			Assert.assertNotNull(testEntity.getId()+ " is null",entity);
			Assert.assertEquals(testEntity.getId(),entity.getId());
		}
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
		experiment.setStrategy("Even");

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
		Assert.assertEquals("groupB", resultExperiment.getVariants().get(1).getGroups().get(1).getId());
	}
}
