package org.abs.consumer.managers;

import junit.framework.Assert;
import org.abs.consumer.entities.Experiment;
import org.abs.consumer.entities.Group;
import org.abs.consumer.entities.Variant;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/16/14
 * Time: 11:50 PM
 */
public class ExperimentManagerTest extends Assert {
	@Before
	public void setUp() throws Exception {
		Jedis jedis = StorageManager.getInstance().getPool().getResource();
		jedis.flushDB();
		StorageManager.getInstance().getPool().returnResource(jedis);
	}

	@Test
	public void testGetExperiments() throws Exception {
		List<Experiment> expected = generateExperiments(5);
		List<Experiment> actual = ExperimentManager.getInstance().getExperiments();
		assertEquals(expected.size(),actual.size());
	}

	private List<Experiment> generateExperiments(int count){
		List<Experiment> experiments = new ArrayList<Experiment>();
		for (int X = count; X>=0; X--){
			List<Group> groups = new ArrayList<Group>();
			groups.add(new Group("groupA" + X));
			groups.add(new Group("groupB" + X));

			List<Variant> variants = new ArrayList<Variant>();
			Variant variant1 = new Variant("variant1" + X);
			variant1.setGroups(groups);
			variants.add(variant1);

			Variant variant2 = new Variant("variant2" + X);
			variant2.setGroups(groups);
			variants.add(variant2);

			Experiment experiment = new Experiment("testExperiment" + X, groups, variants);
			experiments.add(experiment);
		}
		StorageManager.getInstance().saveEntityList(experiments);
		return experiments;
	}
}
