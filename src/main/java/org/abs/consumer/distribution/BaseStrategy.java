package org.abs.consumer.distribution;

import org.abs.consumer.entities.Experiment;
import org.abs.consumer.entities.Persona;
import org.abs.consumer.entities.Variant;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 12:30 AM
 */
public class BaseStrategy implements IStrategy {
	public void assign(Variant variant, Persona persona) {
		persona.addVariant(variant.getId(),variant.getControl());
	}

	public IStrategy getInstance() {
		return null;
	}
}