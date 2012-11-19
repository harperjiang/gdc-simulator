package edu.clarkson.gdc.simulator.impl;

import edu.clarkson.gdc.simulator.framework.Clock;
import edu.clarkson.gdc.simulator.impl.stat.StatisticListener;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Init the cloud
		DefaultCloud.getInstance();

		DefaultCloud.getInstance().addNodeListener(new StatisticListener());

		long stop = 3600 * 1000 * 24 * 365;
		while (Clock.getInstance().getCounter() < stop) {
			Clock.getInstance().step();
		}
	}
}
