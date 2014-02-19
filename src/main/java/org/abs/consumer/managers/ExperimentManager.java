package org.abs.consumer.managers;

import org.abs.consumer.entities.Experiment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 2:22 AM
 */
public class ExperimentManager extends BaseManager {
	private static ExperimentManager instance = null;
	private List<Experiment> experimentList = new ArrayList<Experiment>();
	private long ttl = 300000L;
	private long expires = 0;

	private ExperimentManager() {
		loadExperiments();
	}

	public static ExperimentManager getInstance() {
		if (null == instance){
			instance = new ExperimentManager();
		}
		return instance;
	}

	private void loadExperiments(){
		long now = System.currentTimeMillis();
		if (expires < now){
			experimentList = StorageManager.getInstance().loadEntityList(Experiment.class);
			expires = now + ttl;
			for (Experiment experiment : experimentList){
				experiment.setExpires(expires);
			}
			log.info("Reloaded all experiments. " + experimentList.size());
		}
	}

	public List<Experiment> getExperiments(){
		return experimentList;
	}
}
