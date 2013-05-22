package edu.clarkson.gdc.simulator.scenario.solar;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import edu.clarkson.gdc.simulator.framework.Environment;

public class ScenarioPowerGeneration {

	/**
	 * The unit in this environment is 10ms
	 */
	public static void main(String[] args) {
		final Environment solarenv = new Environment();

		List<SolarServer> solarservers = new ArrayList<SolarServer>();
		solarservers.add(new SolarServer("egypt", new Point2D.Double(24.68695,
				27.42188)));
		solarservers.add(new SolarServer("tibet", new Point2D.Double(38.75408,
				82.88086)));
		solarservers.add(new SolarServer("oceania", new Point2D.Double(
				-22.91792, 127.61719)));
		solarservers.add(new SolarServer("us", new Point2D.Double(34.01624,
				-115.66406)));
		solarservers.add(new SolarServer("brazil", new Point2D.Double(-5.26601,
				-39.90234)));

		for (SolarServer ss : solarservers)
			solarenv.add(ss);

		while (solarenv.getClock().getCounter() < 9640000) {
			solarenv.getClock().tick();
			if (solarenv.getClock().getCounter() % 10000l == 0) {
				long powersum = 0;
				for (SolarServer ss : solarservers) {
					powersum += ss.getPower();
				}
				System.out.println(powersum);
			}
		}

	}
}
