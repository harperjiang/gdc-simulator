package edu.clarkson.gdc.simulator.impl.solar;

import java.awt.geom.Point2D;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.framework.Environment;
import edu.clarkson.gdc.simulator.framework.FailMessage;
import edu.clarkson.gdc.simulator.framework.NodeMessageEvent;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.ProcessTimeModel.ConstantTimeModel;
import edu.clarkson.gdc.simulator.impl.Averager;
import edu.clarkson.gdc.simulator.impl.SectionAverager;
import edu.clarkson.gdc.simulator.impl.SectionAverager.Section;

public class ScenarioSolarLatency {

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

		SolarClient client = new SolarClient();
//		client.setLocation(new Point2D.Double(64.14, -21.87));
		// Reykyavik, Iceland
		 client.setLocation(new Point2D.Double(45.6, -73.7));//
		// Montreal,Canada
//		 client.setLocation(new Point2D.Double(39.93, 116.46));
		// Beijing, China
		solarenv.add(client);
		client.addListener(NodeMessageListener.class,
				solarenv.getProbe(NodeMessageListener.class));

		for (SolarServer ss : solarservers) {
			DistancePipe dp = new DistancePipe(client, ss);
//			System.out.println(ss.getId() + "\t"
//					+ ((ConstantTimeModel) dp.getTimeModel()).getLatency());
		}

		final SectionAverager success = new SectionAverager(36000l);
		final Averager average = new Averager();
		final SectionAverager failed = new SectionAverager(36000l);
		solarenv.addListener(NodeMessageListener.class,
				new NodeMessageListener() {

					@Override
					public void messageReceived(NodeMessageEvent event) {
						if (event.getSource() instanceof Client) {
							if (event.getMessage() instanceof FailMessage) {
								System.out.println(MessageFormat.format(
										"{0}:Failed received from {1}",
										solarenv.getClock().getCounter(), event
												.getMessage().getOrigin()
												.getId()));
								failed.add(solarenv.getClock().getCounter(), 1l);
							} else {
								SolarClientResponse resp = (SolarClientResponse) event
										.getMessage();
								long time = resp.getReceiveTime()
										- resp.getRequest().getSendTime();
								success.add(solarenv.getClock().getCounter(),
										time);
								average.add(time);
							}
						}
					}

					@Override
					public void messageSent(NodeMessageEvent event) {

					}

					@Override
					public void messageTimeout(NodeMessageEvent event) {
						failed.add(solarenv.getClock().getCounter(), 1l);
						return;
					}
				});

		solarenv.run(8640000);

		success.end(solarenv.getClock().getCounter());
		failed.end(solarenv.getClock().getCounter());
		 System.out.println(average.getAverage());
//		for (int i = 0; i < success.getSections().size(); i++) {
//			Section sec = success.getSections().get(i);
//			Section f = failed.getSections().get(i);
//			System.out.println(MessageFormat.format("{0}\t{1}\t{2}\t{3}",
//					sec.start, sec.count, sec.average, f.count));
//		}
	}
}
