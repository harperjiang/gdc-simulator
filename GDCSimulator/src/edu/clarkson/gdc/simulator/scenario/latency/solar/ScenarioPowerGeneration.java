package edu.clarkson.gdc.simulator.scenario.latency.solar;

import java.awt.geom.Point2D;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.clarkson.gdc.simulator.framework.Environment;

public class ScenarioPowerGeneration {

	/**
	 * The unit in this environment is 10ms
	 */
	public static void main(String[] args) throws Exception {
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

		PrintWriter pw = new PrintWriter(
				new FileOutputStream("solar_power_gen"));
		while (solarenv.getClock().getCounter() <= 8640000) {
			solarenv.getClock().tick();
			if (solarenv.getClock().getCounter() % 10000l == 0) {
				double time = ((double) solarenv.getClock().getCounter()) / 360000;
				long powersum = 0;
				pw.print(String.valueOf(time));
				pw.print("\t");
				for (SolarServer ss : solarservers) {
					pw.print(String.valueOf((ss.getPower())));
					pw.print("\t");
					powersum += (ss.getPower());
				}
				pw.println(powersum);
			}
		}
		pw.close();

	}
}