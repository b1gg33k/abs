package org.abs.consumer.entities;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/16/14
 * Time: 5:10 PM
 */
public class Group extends BaseEntity {
	String value = null;

	public Group() {
	}

	public Group(String id) {
		super(id);
	}

	public Group(String id, String value) {
		super(id);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
