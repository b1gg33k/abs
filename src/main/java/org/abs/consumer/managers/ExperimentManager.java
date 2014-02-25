package org.abs.consumer.managers;

import org.abs.consumer.entities.Experiment;
import org.abs.consumer.persistance.EntityDAO;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 2:22 AM
 */
public class ExperimentManager extends BaseManager {
	protected static Logger log = Logger.getLogger(ExperimentManager.class);

	private static ExperimentManager instance = null;
	private List<Experiment> experimentList = new ArrayList<Experiment>();
	private long ttl = 300000L;
	private long expires = 0;

	private ExperimentManager() {
		super();
		String ttlString = null;
		ttlString = PropertiesManager.getInstance().getProperties().getProperty("experiment.manager.ttl");
		if (null != ttlString){
			ttl = Long.parseLong(ttlString);
		}
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
			Map<String,Experiment> experimentMap = EntityDAO.getInstance().loadEntityMap(Experiment.class);
			List<Experiment> newExperimentList = new ArrayList<Experiment>();
			for (String key : experimentMap.keySet()){
				newExperimentList.add(experimentMap.get(key));
			}
			synchronized (experimentList){
				experimentList = newExperimentList;
			}
			expires = now + ttl;
			for (Experiment experiment : experimentList){
				experiment.setExpires(expires);
			}
			log.info(now + "+" + ttl + " Reloaded all experiments. [" + experimentList.size() + "] expires " + expires);
		} else {
			log.info(now + "+" + ttl + " Reloaded NO experiments. [" + experimentList.size() + "] expires " + expires);

		}
	}

	public List<Experiment> getExperiments(){
		loadExperiments();
		return experimentList;
	}


}
