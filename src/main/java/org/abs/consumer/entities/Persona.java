package org.abs.consumer.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/12/14
 * Time: 11:57 PM
 */
public class Persona extends BaseEntity implements IEntity {
	private List<Experiment> experiments = null;
	private Map<String, PersonaVariant> variants = null;

	public Persona() {
	}

	public Persona(String id) {
		super(id);
	}

	public Persona(String id, ArrayList<Experiment> experiments) {
		super(id);
		this.experiments = experiments;
	}

	public List<Experiment> getExperiments() {
		return experiments;
	}

	public void setExperiments(ArrayList<Experiment> experiments) {
		changed = true;
		this.experiments = experiments;
	}

	public Map<String, PersonaVariant> getVariants() {
		return variants;
	}

	public void setVariants(Map<String, PersonaVariant> variants) {
		changed = true;
		if (null == variants){
			this.variants = new HashMap<String, PersonaVariant>();
		} else {
			this.variants = variants;
		}
	}

	public void addVariant(PersonaVariant variant){
		changed = true;
		if (null == variants){
			variants = new HashMap<String, PersonaVariant>();
		}
		variants.put(variant.getId(), variant);
	}

	public void addExperiment(Experiment experiment){
		if (null == experiments){
			experiments = new ArrayList<Experiment>();
		}
		experiments.add(experiment);
	}

	public PersonaVariant getVariant(String varientId){
		return (variants.containsKey(varientId)) ? variants.get(varientId) : null;
	}

}
