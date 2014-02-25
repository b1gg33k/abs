package org.abs.consumer.persistance;

import junit.framework.Assert;
import org.abs.consumer.entities.*;
import org.abs.consumer.persistance.EntityDAO;
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
public class EntityDAOTest {
	@Before
	public void setUp() throws Exception {
		Jedis jedis = EntityDAO.getInstance().getPool().getResource();
		jedis.flushDB();
		EntityDAO.getInstance().getPool().returnResource(jedis);
	}

	@Test
	public void testSaveMap() throws Exception {
		Map<String,IEntity> testMap = new HashMap<String, IEntity>();
		testMap.put("1", new TestEntity("one"));
		testMap.put("2", new TestEntity("two"));
		testMap.put("3", new TestEntity("three"));
		testMap.put("4", new TestEntity("four"));

		EntityDAO entityDAO = EntityDAO.getInstance();
		entityDAO.saveEntityMap(testMap);
		Map<String,TestEntity> resultsMap = entityDAO.loadEntityMap(TestEntity.class);

		for (String key : testMap.keySet()){
			Assert.assertTrue(resultsMap.containsKey(key));
			Assert.assertEquals(testMap.get(key).getId(),resultsMap.get(key).getId());
		}
		Assert.assertNotNull("john");

	}

	@Test
	public void testLoadEntityMap() throws Exception {
		//We hae this to test if resources are returned to the pool...
		//The test won't fail, it just wont ever end.
		for (int X = 0; X<=500; X++){
			Map<String,TestEntity> theMap = EntityDAO.getInstance().loadEntityMap(TestEntity.class);
		}
	}

	@Test
	public void testSaveLoadEntity() throws Exception {
		Map<String,Group> groups = new HashMap<String, Group>();
		groups.put("groupA", new Group("groupA"));
		groups.put("groupB", new Group("groupB"));

		List<Variant> variants = new ArrayList<Variant>();
		Variant variant1 = new Variant("variant1");
		variant1.setGroups(groups);
		variants.add(variant1);

		Variant variant2 = new Variant("variant2");
		variant2.setGroups(groups);
		variants.add(variant2);

		Experiment experiment = new Experiment("testExperiment", groups, variants);
		experiment.setStrategy("Even");

		EntityDAO.getInstance().saveEntity(experiment);
		Experiment resultExperiment = EntityDAO.getInstance().loadEntity(experiment.getId(),Experiment.class);

		Assert.assertNotNull(resultExperiment);
		Assert.assertNotNull(resultExperiment.getGroups().get("groupA"));
		Assert.assertNotNull(resultExperiment.getGroups().get("groupB"));
		Assert.assertEquals("variant1", resultExperiment.getVariants().get(0).getId());
		Assert.assertEquals("variant2", resultExperiment.getVariants().get(1).getId());

		Assert.assertNotNull(resultExperiment.getVariants().get(0).getGroups().get("groupA"));
		Assert.assertNotNull(resultExperiment.getVariants().get(0).getGroups().get("groupB"));
		Assert.assertNotNull(resultExperiment.getVariants().get(1).getGroups().get("groupA"));
		Assert.assertNotNull(resultExperiment.getVariants().get(1).getGroups().get("groupB"));
	}

}
