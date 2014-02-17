package org.abs.consumer.entities;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/15/14
 * Time: 11:01 PM
 */
public abstract class BaseEntity implements IEntity {
	static Logger log = Logger.getLogger(BaseEntity.class.getName());

	protected String id = null;
	protected transient boolean changed = false;
	protected transient long expires = 0;

	protected BaseEntity(){

	}

	public BaseEntity(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		changed = true;
		this.id = id;
	}
	@Override
	public String toJson() {
		return new JSONObject(this).toString();
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

	@Override
	public String toString(){
		String me = this.getClass().getName();
		for (Method method : this.getClass().getDeclaredMethods()){
			if (method.getName().startsWith("get") && method.getParameterTypes().length == 0){
				String first = method.getName().substring(3,4);
				String name = first + method.getName().substring(4);
				try {
						me += " " + name + "=" + method.invoke(this).toString() + "\t";
				} catch (Exception e) {
					log.warn("could not convert " + method.getName() + " to a string. " + e.getLocalizedMessage());
				}
			}
		}
		return me;
	}
}
