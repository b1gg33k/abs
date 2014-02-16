package org.abs.consumer.managers;

import org.abs.consumer.entities.Experiment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 2:22 AM
 */
public class ExperimentManager extends BaseManager {
	private static ExperimentManager instance = new ExperimentManager();
	private static Map<String,Experiment> experimentMap = new HashMap<String, Experiment>();
	private long ttl = 300000L;

	private ExperimentManager() {
		loadExperiments();
	}

	public static ExperimentManager getInstance() {
		return instance;
	}

	private void loadExperiments(){
		//TODO: Load from redis.
	}

	public long getTtl() {
		return ttl;
	}

	public void setTtl(long ttl) {
		this.ttl = ttl;
	}
}
