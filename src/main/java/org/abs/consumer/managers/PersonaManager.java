package org.abs.consumer.managers;

import org.abs.consumer.distribution.IStrategy;
import org.abs.consumer.distribution.StrategyFactory;
import org.abs.consumer.entities.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/12/14
 * Time: 11:55 PM
 */
public class PersonaManager extends BaseManager {
	private static PersonaManager instance = null;
	private static Map<String,Persona> personaCache = new HashMap<String, Persona>();
	private ExperimentManager experimentManager = ExperimentManager.getInstance();

	public static PersonaManager getInstance() {
		if (null == instance){
			instance = new PersonaManager();
		}
		return instance;
	}

	public void addExperiment(Persona persona, Experiment experiment) {
		StrategyFactory strategyFactory = StrategyFactory.getInstance();
		IStrategy strategy = strategyFactory.getStrategy(experiment.getStrategy());
		persona.addExperiment(experiment);

		Group activeGroup = null;
		for (Variant variant : experiment.getVariants()) {
			if (null == activeGroup){
				activeGroup = strategy.assign(variant, persona);
			} else {
				PersonaVariant personaVariant = new PersonaVariant(variant,activeGroup);
				persona.addVariant(personaVariant);
			}
		}
	}
	public void addExperiments(Persona persona, List<Experiment> experiments) {
		for(Experiment experiment : experiments){
			addExperiment(persona,experiment);
		}
	}

	public Persona loadPersona(String personaId){
		Persona persona = StorageManager.getInstance().loadEntity(personaId,Persona.class);
		if (null == persona){
			persona = new Persona(personaId);
			addExperiments(persona,ExperimentManager.getInstance().getExperiments());
		}

		return persona;
	}

	public void savePersona(Persona persona){
		StorageManager.getInstance().saveEntity(persona);
	}
}
