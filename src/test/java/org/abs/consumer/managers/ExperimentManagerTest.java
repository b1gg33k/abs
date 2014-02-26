package org.abs.consumer.managers;

import junit.framework.Assert;
import org.abs.consumer.entities.Experiment;
import org.abs.consumer.entities.Group;
import org.abs.consumer.entities.Variant;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		for (Experiment experiment : expected){
			String meh = "\n";
			boolean found = false;
			for (Experiment experiment1 : actual){
				meh += experiment1.toJson() + "||||||||||||" + actual.size();
				if (experiment.getId().equals(experiment1.getId())){
					found = true;
					continue;
				}
			}
			assertTrue("Experiment " + experiment.getId() + " Was not found. " + meh, found);
		}
	}

	private List<Experiment> generateExperiments(int count){
		Map<String,Experiment> experiments = new HashMap<String, Experiment>();
		List<Experiment> experimentList = new ArrayList<Experiment>();
		for (int X = count; X>=0; X--){
			Map<String,Group> groups = new HashMap<String,Group>();
			groups.put("groupA" + X,new Group("groupA" + X));
			groups.put("groupB" + X, new Group("groupB" + X));

			List<Variant> variants = new ArrayList<Variant>();
			Variant variant1 = new Variant("variant1" + X);
			variant1.setGroups(groups);
			variants.add(variant1);

			Variant variant2 = new Variant("variant2" + X);
			variant2.setGroups(groups);
			variants.add(variant2);

			Experiment experiment = new Experiment("testExperiment" + X, groups, variants);
			experimentList.add(experiment);
			experiments.put(experiment.getId(),experiment);
		}
		StorageManager.getInstance().saveEntityMap(experiments);
		return experimentList;
	}
}
