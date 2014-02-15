package org.abs.consumer.entities;

import java.io.Serializable;
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
public class Persona implements Serializable {

	private String id = null;
	private List<String> experiments = new ArrayList<String>();
	private Map<String,Serializable> variants = new HashMap<String,Serializable>();

	private transient boolean changed = false;
	private transient long expires = 0;

	public Persona(String id) {
		this.id = id;
	}

	public Persona(String id, ArrayList<String> experiments) {
		this.id = id;
		this.experiments = experiments;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		changed = true;
		this.id = id;
	}

	public List<String> getExperiments() {
		return experiments;
	}

	public void setExperiments(ArrayList<String> experiments) {
		changed = true;
		this.experiments = experiments;
	}

	public Map<String, Serializable> getVariants() {
		return variants;
	}

	public void setVariants(Map<String, Serializable> variants) {
		changed = true;
		if (null == variants){
			this.variants = new HashMap<String, Serializable>();
		} else {
			this.variants = variants;
		}
	}

	public void addVariant(String variantName, Serializable variantValue){
		changed = true;
		if (null != variants){
			variants.put(variantName, variantValue);
		}
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}
}
