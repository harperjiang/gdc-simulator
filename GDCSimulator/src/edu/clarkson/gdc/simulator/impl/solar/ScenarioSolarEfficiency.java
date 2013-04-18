package edu.clarkson.gdc.simulator.impl.solar;

import java.awt.geom.Point2D;
import java.text.MessageFormat;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.framework.Environment;
import edu.clarkson.gdc.simulator.framework.NodeMessageEvent;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.TimeoutMessage;
import edu.clarkson.gdc.simulator.impl.Averager;

public class ScenarioSolarEfficiency {

	/**
	 * The unit in this environment is 10ms
	 */
	public static void main(String[] args) {
		Environment solarenv = new Environment();

//		SolarServer egypt = new SolarServer("egypt", new Point2D.Double(
//				24.68695, 27.42188));
		SolarServer tibet = new SolarServer("tibet", new Point2D.Double(
				38.75408, 82.88086));
//		SolarServer oceania = new SolarServer("oceania", new Point2D.Double(
//				-22.91792, 127.61719));
//		SolarServer us = new SolarServer("us", new Point2D.Double(34.01624,
//				-115.66406));
//		SolarServer brazil = new SolarServer("brazil", new Point2D.Double(
//				-5.26601, -39.90234));

//		solarenv.add(egypt);
		solarenv.add(tibet);
//		solarenv.add(oceania);
//		solarenv.add(us);
//		solarenv.add(brazil);

		SolarClient client = new SolarClient();
//		client.setLocation(new Point2D.Double(66.4, -45.5));// Green Land
		client.setLocation(new Point2D.Double(55.4, -109.9));// Canada
		solarenv.add(client);
		client.addListener(NodeMessageListener.class,
				solarenv.getProbe(NodeMessageListener.class));

//		new DistancePipe(client, egypt);
		new DistancePipe(client, tibet);
//		new DistancePipe(client, oceania);
//		new DistancePipe(client, us);
//		new DistancePipe(client, brazil);

		final Averager success = new Averager();
		final Averager failed = new Averager();
		solarenv.addListener(NodeMessageListener.class,
				new NodeMessageListener() {

					@Override
					public void messageReceived(NodeMessageEvent event) {
						if (event.getSource() instanceof Client) {
							if (event.getMessage() instanceof TimeoutMessage) {
								failed.add(1l);
								return;
							}
							SolarClientResponse resp = (SolarClientResponse) event
									.getMessage();
							long time = resp.getReceiveTime()
									- resp.getRequest().getSendTime();
							success.add(time);
						}
					}

					@Override
					public void messageSent(NodeMessageEvent event) {

					}
				});

		solarenv.run(8640000);

		System.out.println(MessageFormat.format(
				"Success {0}, Average {1}, Failed {2}", success.getCount(),
				success.getAverage(), failed.getCount()));
	}

}
