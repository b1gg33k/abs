package org.abs.consumer.distribution;

import org.abs.consumer.entities.*;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 12:25 AM
 */
public interface IStrategy extends Serializable {
	public Group assign(Variant variant, Persona persona);
}
