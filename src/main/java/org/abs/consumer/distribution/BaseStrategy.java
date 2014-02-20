package org.abs.consumer.distribution;

import org.abs.consumer.entities.Group;
import org.abs.consumer.entities.Persona;
import org.abs.consumer.entities.PersonaVariant;
import org.abs.consumer.entities.Variant;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 12:30 AM
 */

/* assign none */
public class BaseStrategy implements IStrategy {
	public Group assign(Variant variant, Persona persona) {
		return null;
	}

	public IStrategy getInstance() {
		return null;
	}
}