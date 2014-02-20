package org.abs.consumer.managers;

import junit.framework.Assert;
import org.abs.consumer.distribution.StrategyFactory;
import org.abs.consumer.entities.Experiment;
import org.abs.consumer.entities.Group;
import org.abs.consumer.entities.Persona;
import org.abs.consumer.entities.Variant;
import org.junit.Before;
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
public class PersonaManagerTest extends Assert {
	@Before
	public void setUp() throws Exception {
		Jedis jedis = StorageManager.getInstance().getPool().getResource();
		jedis.flushDB();
		StorageManager.getInstance().getPool().returnResource(jedis);
	}

	@Test
	public void testAddExperiment() throws Exception {
		Experiment experiment = createExperiment();
		StorageManager.getInstance().saveEntity(experiment);

		Persona persona = new Persona("testUser");

		PersonaManager.getInstance().addExperiment(persona, experiment);


		//Assert.assertEquals(1, persona.getExperiments().size());
		//Assert.assertEquals("groupA", persona.getVariant("variant1").getActiveGroup().getId());
		//Assert.assertTrue("Persona does not have the inactive group on variant2.", persona.getVariant("variant2").getGroups().contains(groups1.get(0)));

	}

	@Test
	public void testSaveLoadPersona() throws Exception {
		StorageManager.getInstance().saveEntity(createExperiment());

		Persona persona = PersonaManager.getInstance().loadPersona("lkkjasdfklsdaflkjasdflkj");
		assertNotNull(persona.getExperiments());
		assertNotNull(persona.getVariants());
		assertTrue(persona.getExperiments().size() > 0);
		assertTrue(persona.getVariants().size() > 0);
	}

	private Experiment createExperiment(){
		Map<String,Group> groups1 = new HashMap<String, Group>();
		groups1.put("groupA", new Group("groupA", "http://img2.timeinc.net/ew/i/2013/07/11/JUSTIN-BIEBER.jpg"));
		groups1.put("groupB", new Group("groupB", "http://www.billboard.com/files/styles/promo_650/public/media/miley_cyrus_2013-650-430c.jpg"));

		List<Variant> variants = new ArrayList<Variant>();
		Variant variant1 = new Variant("variant1");
		variant1.setGroups(groups1);
		variants.add(variant1);

		Map<String, Group> groups2 = new HashMap<String,Group>();
		groups2.put("groupA", new Group("groupA", "red"));
		groups2.put("groupB", new Group("groupB", "blue"));

		Variant variant2 = new Variant("variant2");
		variant2.setGroups(groups2);
		variants.add(variant2);

		Experiment experiment = new Experiment("testExperiment", groups1, variants);
		experiment.setStrategy("Even");
		StorageManager.getInstance().saveEntity(experiment);
		experiment.setStrategy("Even");

		return experiment;
	}
}
