package edu.clarkson.gdc.simulator.impl;

import org.apache.commons.lang.time.StopWatch;

import edu.clarkson.gdc.simulator.impl.stat.StatisticListener;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Init the cloud
		DefaultCloud cloud = new DefaultCloud();

		StopWatch watch = new StopWatch();

		StatisticListener listener = new StatisticListener();
		cloud.addNodeListener(listener);

		long stop = 3600 * 24 * 10;

		watch.start();

		cloud.run(stop);

		watch.stop();

		System.out.println(listener.getReport().getRequestCount());
		System.out.println(listener.getReport().getFailedCount());
		System.out.println(listener.getReport().getAvailability());
		System.out.println(watch.getTime());
	}
}
