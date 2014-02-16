package org.abs.consumer.entities;

import org.abs.consumer.distribution.IStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 12:00 AM
 */
public class Experiment extends BaseEntity implements IEntity {
	private static final long serialVersionUID = -2883767302035068380L;
	private List<Variant> variants = new ArrayList<Variant>();

	public Experiment(String id, List<Variant> variants, Class<IStrategy> strategy) {
		super(id);
		this.variants = variants;
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


}
