package org.abs.consumer.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 12:00 AM
 */
public class Experiment extends BaseEntity implements IEntity {
	private static final long serialVersionUID = -2883767302035068380L;
	private List<Variant> variants = new ArrayList<Variant>();
	private List<Group> groups = new ArrayList<Group>();
	private String strategy = null;

	public Experiment() {
	}

	public Experiment(String id) {
		super(id);
	}

	public Experiment(String id, List<Group> groups, List<Variant> variants, String strategy) {
		super(id);
		this.variants = variants;
		this.groups = groups;
		this.strategy = strategy;
	}

	public Experiment(String id, List<Group> groups, List<Variant> variants) {
		super(id);
		this.variants = variants;
		this.groups = groups;
	}

	public List<Variant> getVariants() {
		return variants;
	}

	public void setVariants(List<Variant> variants) {
		if (null == variants){
			this.variants = new ArrayList<Variant>();
		} else {
			this.variants = variants;
		}
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

}
