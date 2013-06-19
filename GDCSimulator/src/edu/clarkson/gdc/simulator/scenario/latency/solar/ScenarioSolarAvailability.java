package edu.clarkson.gdc.simulator.scenario.latency.solar;

import java.awt.geom.Point2D;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import edu.clarkson.gdc.simulator.framework.Environment;

public class ScenarioSolarAvailability {

	/**
	 * The unit in this environment is 10ms
	 */
	public static void main(String[] args) throws Exception {

		Environment env = new Environment();

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
		for (SolarServer ss : solarservers) {
			env.add(ss);
		}

		PrintWriter pw = new PrintWriter(
				new FileOutputStream("solar_ds_online"));

		for (int i = 0; i < 8640000; i++) {
			if (env.getClock().getCounter() % 10000l == 0) {
				int sum = 0;
				for (int j = 0; j < solarservers.size(); j++) {
					if (solarservers.get(j).getExceptionStrategy()
							.getException(i) == null)
						sum++;
				}

				double start = ((double) i) / 360000;
				pw.println(MessageFormat.format("{0}\t{1}",
						Double.toString(start), sum));
			}
			env.getClock().tick();
		}
		pw.close();
	}
}
