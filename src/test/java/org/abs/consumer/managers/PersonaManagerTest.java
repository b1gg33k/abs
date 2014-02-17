package org.abs.consumer.managers;

import junit.framework.Assert;
import org.abs.consumer.distribution.StrategyFactory;
import org.abs.consumer.entities.Experiment;
import org.abs.consumer.entities.Group;
import org.abs.consumer.entities.Persona;
import org.abs.consumer.entities.Variant;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/16/14
 * Time: 11:50 PM
 */
public class PersonaManagerTest {
	@Test
	public void testAddExperiment() throws Exception {
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
		Persona persona = new Persona("testUser");

		PersonaManager.getInstance().addExperiment(persona, experiment);

		Assert.assertEquals(1, persona.getExperiments().size());
		Assert.assertEquals("groupA", persona.getVariant("variant1").getActiveGroup().getId());
		Assert.assertTrue("Persona does not have the inactive group on variant2.", persona.getVariant("variant2").getGroups().contains(groups.get(0)));

	}
}
