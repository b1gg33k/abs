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
	IStrategy defaultStrategy = new BaseStrategy();
	Map<String, IStrategy> strategies = new HashMap<String, IStrategy>();

	public StrategyFactory() {
		strategies.put("Percentage", new PercentageStrategy());
	}

	public IStrategy getStrategy(String strategyName) {
		IStrategy strategy = defaultStrategy;
		if (null != strategies && strategies.containsKey(strategyName)) {
			strategy = strategies.get(strategyName);
		}
		return strategy;
	}

	public void addExperiment(Experiment experiment, Persona persona) {
		for (Variant variant : experiment.getVariants()) {
			IStrategy strategy = getStrategy(variant.getStrategy());
			strategy.assign(variant, persona);
		}
	}
}
