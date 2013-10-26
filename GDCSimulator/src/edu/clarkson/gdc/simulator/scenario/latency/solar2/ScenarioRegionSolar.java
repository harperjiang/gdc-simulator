package edu.clarkson.gdc.simulator.scenario.latency.solar2;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import edu.clarkson.gdc.simulator.module.network.LatencyEstimator;
import edu.clarkson.gdc.simulator.module.network.impl.WorldLatencyEstimator;

public class ScenarioRegionSolar {

	public static void main(String[] args) throws Exception {
		// Time Choose Jan 1
		LatencyEstimator le = new WorldLatencyEstimator();

		List<RegionSolarServer> servers = new ArrayList<RegionSolarServer>();

		RegionSolarServer s1 = new RegionSolarServer();
		s1.setRegionId("NORTH_AMERICA");
		// 32'22'' 112'7''
		s1.setSunrise(14.5);
		s1.setSunset(0.56);
		servers.add(s1);

		// 29.93, 27.43
		RegionSolarServer s2 = new RegionSolarServer();
		s2.setRegionId("NORTH_AFRICA");
		s2.setSunrise(4.85);
		s2.setSunset(15.62);
		servers.add(s2);

		// 37.46, 81.89
		RegionSolarServer s3 = new RegionSolarServer();
		s3.setRegionId("CHINA");
		s3.setSunrise(1.78);
		s3.setSunset(11.42);
		servers.add(s3);

		// -22.218, 133.71
		RegionSolarServer s4 = new RegionSolarServer();
		s4.setRegionId("OCEANIA");
		s4.setSunrise(20.42);
		s4.setSunset(9.88);
		servers.add(s4);

		//
		RegionSolarServer s5 = new RegionSolarServer();
		s5.setRegionId("SOUTH_AMERICA");
		s5.setSunrise(7.82);
		s5.setSunset(20.35);
		servers.add(s5);

		// Client is in NORTH_AMERICA
		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"/home/harper/solar_latency_2"));
		for (int i = 0; i < 2400; i++) {
			double time = i * 0.01;
			long min = 10000;
			for (RegionSolarServer rss : servers) {
				if (rss.available(time)) {
					long latency = le.estimate(le.query("NORTH_AMERICA"),
							le.query(rss.getRegionId()));
					if (latency < min)
						min = latency;
				}
			}
			pw.println(MessageFormat.format("{0}\t{1}", time,
					String.valueOf(min)));
		}
		pw.close();
	}
}
