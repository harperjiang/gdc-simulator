package edu.clarkson.gdc.simulator.impl.latency;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.framework.NodeMessageEvent;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.impl.Averager;

public class ScenarioSingleDCRead {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int clientCount = 1; clientCount < 20; clientCount++) {
			LatencyEnvironment env = new LatencyEnvironment();
			
			IsolateServer server = new IsolateServer() {
				{
					power = 4;
					slowPart = 0;
				}
			};
			env.add(server);

			for (int i = 0; i < clientCount; i++) {
				WaitClient client = new WaitClient();
				new Pipe(client, server);
				env.add(client);
				client.addListener(NodeMessageListener.class,
						env.getProbe(NodeMessageListener.class));
			}

			final Averager all = new Averager();
			final Averager read = new Averager();
			final Averager write = new Averager();

			env.addListener(NodeMessageListener.class,
					new NodeMessageListener() {

						@Override
						public void messageReceived(NodeMessageEvent event) {
							if (event.getSource() instanceof Client
									&& event.getMessage() instanceof ClientResponse) {
								ClientResponse resp = (ClientResponse) event
										.getMessage();
								long time = resp.getReceiveTime()
										- resp.getRequest().getSendTime();
								all.add(time);
								if (resp.getRequest() instanceof ClientRead) {
									read.add(time);
								} else {
									write.add(time);
								}
							}
						}

						@Override
						public void messageSent(NodeMessageEvent event) {

						}

						@Override
						public void messageTimeout(NodeMessageEvent event) {
							// TODO Auto-generated method stub
							
						}
					});

			env.run(86400l);

			System.out.println(clientCount + "\t" + all.getAverage() + "\t"
					+ read.getAverage() + "\t" + write.getAverage());
		}
	}
}
