package org.abs.consumer.distribution;

import org.abs.consumer.entities.Group;
import org.abs.consumer.entities.Persona;
import org.abs.consumer.entities.PersonaVariant;
import org.abs.consumer.entities.Variant;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 12:30 AM
 */

/* The base always choose the first group. */
public class BaseStrategy implements IStrategy {
	public void assign(Variant variant, Persona persona) {
		Group activeGroup = (null != variant.getGroups() && variant.getGroups().size()>0) ? variant.getGroups().get(0) : null;
		PersonaVariant personaVariant = new PersonaVariant(variant, activeGroup);
		persona.addVariant(personaVariant);
	}

	public IStrategy getInstance() {
		return null;
	}
}