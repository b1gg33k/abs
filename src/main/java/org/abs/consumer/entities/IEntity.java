package org.abs.consumer.entities;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/15/14
 * Time: 10:58 PM
 */
public interface IEntity extends Serializable {

	public String getId();

	public void setId(String id);

	public String toJson();
}
