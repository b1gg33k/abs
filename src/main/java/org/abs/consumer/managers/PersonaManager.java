package org.abs.consumer.managers;

import org.abs.consumer.entities.Persona;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/12/14
 * Time: 11:55 PM
 */
public class PersonaManager {
	private PersonaManager instance = new PersonaManager();
	private static Map<String,Persona> personaCache = new HashMap<String, Persona>();
	private ExperimentManager experimentManager = ExperimentManager.getInstance();

	public PersonaManager getInstance() {
		return instance;
	}

}
