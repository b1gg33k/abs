package org.abs.consumer.distribution;

import org.abs.consumer.entities.Group;
import org.abs.consumer.entities.Persona;
import org.abs.consumer.entities.PersonaVariant;
import org.abs.consumer.entities.Variant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 1:03 AM
 */
public class EvenStrategy extends BaseStrategy {

	private static final long serialVersionUID = 6209452217192159816L;

	public Group assign(Variant variant, Persona persona) {
		Group activeGroup = null;
		try {
			SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
			int groupSize = variant.getGroups().size();
			int selected = prng.nextInt(groupSize);

			int X = 0;
			for (String id : variant.getGroups().keySet()){
				if (X == selected){
					activeGroup = variant.getGroups().get(id);
					PersonaVariant personaVariant = new PersonaVariant(variant, activeGroup);
					persona.addVariant(personaVariant);
					break;
				}
				X++;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return activeGroup;
	}
}