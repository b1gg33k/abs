package org.abs.consumer.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/12/14
 * Time: 11:58 PM
 */
public class Variant extends BaseEntity implements IEntity {
	private List<Group> groups = null;

	protected Variant(){

	}

	public Variant(String id) {
		super(id);
		groups = new ArrayList<Group>();
	}

	public Variant(String id, List<Group> groups) {
		super(id);
		this.groups = groups;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public void addGroup(Group group){
		if (null == groups){
			groups = new ArrayList<Group>();
		}
		groups.add(group);
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
