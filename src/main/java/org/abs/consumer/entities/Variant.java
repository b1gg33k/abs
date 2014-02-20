package org.abs.consumer.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/12/14
 * Time: 11:58 PM
 */
public class Variant extends BaseEntity implements IEntity {
	private Map<String,Group> groups = null;

	protected Variant(){

	}

	public Variant(String id) {
		super(id);
		groups = new HashMap<String,Group>();
	}

	public Variant(String id, Map<String,Group> groups) {
		super(id);
		this.groups = groups;
	}

	public Map<String,Group> getGroups() {
		return groups;
	}

	public void setGroups(Map<String,Group> groups) {
		this.groups = groups;
	}

	public void addGroup(Group group){
		if (null == groups){
			groups = new HashMap<String,Group>();
		}
		groups.put(group.getId(), group);
	}

	public void clone(Variant other){
		other.setId(this.getId());
		other.setGroups(this.getGroups());
	}

	@Override
	public Variant clone(){
		Variant other = new Variant();
		clone(other);
		return other;
	}
}
