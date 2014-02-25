package org.abs.consumer.managers;

import junit.framework.Assert;
import org.abs.consumer.entities.Experiment;
import org.abs.consumer.entities.Group;
import org.abs.consumer.entities.Persona;
import org.abs.consumer.entities.Variant;
import org.abs.consumer.persistance.EntityDAO;
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
		Jedis jedis = EntityDAO.getInstance().getPool().getResource();
		jedis.select(EntityDAO.getInstance().getDatabaseIndex());
		jedis.flushDB();
		EntityDAO.getInstance().getPool().returnResource(jedis);
	}

	@Test
	public void testAddExperiment() throws Exception {
		Experiment experiment = createExperiment();
		EntityDAO.getInstance().saveEntity(experiment);

		Persona persona = new Persona("testUser");

		PersonaManager.getInstance().addExperiment(persona, experiment);


		assertEquals(1, persona.getExperiments().size());
		assertEquals(persona.getVariant("variant1").getActiveGroup().getId(),persona.getVariant("variant2").getActiveGroup().getId());
		for (String groupName : persona.getVariant("variant2").getGroups().keySet()){
			Group expected = persona.getVariant("variant1").getGroups().get(groupName);
			Group actual = persona.getVariant("variant2").getGroups().get(groupName);
			assertEquals(expected.getId(),actual.getId());
		}

	}

	@Test
	public void testSaveLoadPersona() throws Exception {
		EntityDAO.getInstance().saveEntity(createExperiment());

		Persona persona = PersonaManager.getInstance().loadPersona("lkkjasdfklsdaflkjasdflkj");
		assertNotNull(persona.getExperiments());
		assertNotNull(persona.getVariants());
		assertTrue(persona.getExperiments().size() > 0);
		assertTrue(persona.getVariants().size() > 0);
	}

	private Experiment createExperiment(){
		Map<String,Group> groups1 = new HashMap<String, Group>();
		groups1.put("groupA", new Group("groupA", "groupA"));
		groups1.put("groupB", new Group("groupB", "groupB"));

		List<Variant> variants = new ArrayList<Variant>();
		Variant variant1 = new Variant("variant1");
		variant1.setGroups(groups1);
		variants.add(variant1);

		Map<String, Group> groups2 = new HashMap<String,Group>();
		groups2.put("groupA", new Group("groupA", "groupA"));
		groups2.put("groupB", new Group("groupB", "groupB"));

		Variant variant2 = new Variant("variant2");
		variant2.setGroups(groups2);
		variants.add(variant2);

		Experiment experiment = new Experiment("testExperiment", groups1, variants);
		experiment.setStrategy("Even");
		EntityDAO.getInstance().saveEntity(experiment);
		experiment.setStrategy("Even");

		return experiment;
	}
}
