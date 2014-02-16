package org.abs.consumer.entities;

import org.abs.consumer.distribution.IStrategy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/12/14
 * Time: 11:58 PM
 */
public class Variant extends BaseEntity implements IEntity {
	private Serializable control = null;
	private Map<String,Serializable> alternates = null;
	private String strategy = null;

	public Variant(String id) {
		super(id);
		alternates = new HashMap<String, Serializable>();
	}

	public Variant(String id, String control, Map<String, Serializable> alternates) {
		super(id);
		this.control = control;
		this.alternates = alternates;
	}

	public Serializable getControl() {
		return control;
	}

	public void setControl(Serializable control) {
		this.control = control;
	}

	public Map<String, Serializable> getAlternates() {
		return alternates;
	}

	public void setAlternates(Map<String, Serializable> alternates) {
		this.alternates = alternates;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
}
