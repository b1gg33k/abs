package org.abs.consumer.distribution;

import junit.framework.Assert;
import org.abs.consumer.entities.Group;
import org.abs.consumer.entities.Persona;
import org.abs.consumer.entities.Variant;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/16/14
 * Time: 5:29 PM
 */
public class StrategyTest {
	protected static Logger log = Logger.getLogger(StrategyTest.class);

	@Test
	public void testAssign() throws Exception {
		BaseStrategy baseStrategy = new BaseStrategy();
		Variant variant = new Variant("testVarient");
		variant.addGroup(new Group("GroupA"));
		variant.addGroup(new Group("GroupB"));

		Persona persona = new Persona("testUser");
		baseStrategy.assign(variant,persona);
		log.debug("Persona: " + persona.getId() + " = " + persona.getVariant("testVarient"));
		Assert.assertEquals("GroupA", persona.getVariant("testVarient").getActiveGroup().getId());
	}
}
