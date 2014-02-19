package org.abs.consumer.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

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

	public BaseEntity(){

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
	public String toJson() throws JsonProcessingException {
		return this.toJson(false);
	}

	public String toJson(boolean pretty) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String result = null;
		if (pretty){
			result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} else {
			result = mapper.writeValueAsString(this);
		}
		return result;
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
