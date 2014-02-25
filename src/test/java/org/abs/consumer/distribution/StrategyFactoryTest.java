package org.abs.consumer.distribution;

import junit.framework.Assert;
import org.abs.consumer.entities.Experiment;
import org.abs.consumer.entities.Group;
import org.abs.consumer.entities.Persona;
import org.abs.consumer.entities.Variant;
import org.abs.consumer.persistance.EntityDAO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/16/14
 * Time: 4:58 PM
 */
public class StrategyFactoryTest {
	@Test
	public void addEvenTest(){
		Map<String,Group> groups = new HashMap<String, Group>();
		groups.put("groupB",new Group("groupB"));

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

		Persona persona = new Persona("testUser");

		StrategyFactory.getInstance().addExperiment(experiment,persona);
		Assert.assertEquals(experiment.getId(), persona.getExperiments().get(0).getId());
	}
}
