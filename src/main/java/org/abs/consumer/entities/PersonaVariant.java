package org.abs.consumer.entities;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/16/14
 * Time: 10:29 PM
 */
public class PersonaVariant extends Variant {
	private Group activeGroup = null;

	public PersonaVariant() {
	}

	public Group getActiveGroup() {
		return activeGroup;
	}

	public void setActiveGroup(Group activeGroup) {
		this.activeGroup = activeGroup;
	}

	public PersonaVariant(Variant variant, Group activeGroup){
		variant.clone(this);
		this.setActiveGroup(activeGroup);
	}
}
