package org.abs.consumer.distribution;

import org.abs.consumer.entities.Experiment;
import org.abs.consumer.entities.Persona;
import org.abs.consumer.entities.PersonaVariant;
import org.abs.consumer.entities.Variant;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 12:25 AM
 */
public interface IStrategy extends Serializable {
	public void assign(Variant variant, Persona persona);
}
