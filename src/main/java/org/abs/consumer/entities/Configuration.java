package org.abs.consumer.entities;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 2:46 AM
 */
public class Configuration extends BaseEntity implements IEntity {
	private Long experimentTTL = null;
	private Long PersonaTTL = null;
	private Map<String,Configuration> hostConfigurations = null;

	public Configuration() {
	}

	protected Configuration(String id) {
		super(id);
	}

	public Long getExperimentTTL() {
		return experimentTTL;
	}

	public void setExperimentTTL(Long experimentTTL) {
		this.experimentTTL = experimentTTL;
	}

	public Long getPersonaTTL() {
		return PersonaTTL;
	}

	public void setPersonaTTL(Long personaTTL) {
		PersonaTTL = personaTTL;
	}

	public Map<String, Configuration> getHostConfigurations() {
		return hostConfigurations;
	}

	public void setHostConfigurations(Map<String, Configuration> hostConfigurations) {
		this.hostConfigurations = hostConfigurations;
	}
}
