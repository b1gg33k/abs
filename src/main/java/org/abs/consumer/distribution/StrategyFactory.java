package org.abs.consumer.distribution;

import org.abs.consumer.entities.Experiment;
import org.abs.consumer.entities.Persona;
import org.abs.consumer.entities.Variant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 1:21 AM
 */
public class StrategyFactory {
	private static final StrategyFactory instance = new StrategyFactory();
	IStrategy defaultStrategy = new BaseStrategy();
	Map<String, IStrategy> strategies = new HashMap<String, IStrategy>();

	private StrategyFactory() {
		strategies.put("Even", new EvenStrategy());
	}

	public static StrategyFactory getInstance() {
		return instance;
	}

	public IStrategy getDefaultStrategy() {
		return defaultStrategy;
	}

	public IStrategy getStrategy(String strategyName) {
		IStrategy strategy = null;
		if (null != strategies && strategies.containsKey(strategyName)) {
			strategy = strategies.get(strategyName);
		} else {
			strategy = defaultStrategy;
		}
		return strategy;
	}

	public void addExperiment(Experiment experiment, Persona persona) {
        IStrategy strategy = getStrategy(experiment.getStrategy());
		persona.addExperiment(experiment);
		if (null == strategy){
			strategy = defaultStrategy;
		}
		for (Variant variant : experiment.getVariants()) {
			strategy.assign(variant, persona);
		}
	}
}
