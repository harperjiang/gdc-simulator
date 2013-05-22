package edu.clarkson.gdc.simulator.scenario.latency.readwrite;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.framework.NodeMessageEvent;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.storage.DefaultCacheStorage;
import edu.clarkson.gdc.simulator.module.message.ClientRead;
import edu.clarkson.gdc.simulator.module.message.ClientResponse;
import edu.clarkson.gdc.simulator.module.server.isolate.IsolateServer;
import edu.clarkson.gdc.simulator.scenario.Averager;

public class ScenarioSingleDCRead {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DefaultCacheStorage storage = new DefaultCacheStorage();
		storage.setReadTime(50);
		storage.setWriteTime(70);

		for (int clientCount = 1; clientCount < 100; clientCount++) {
			LatencyEnvironment env = new LatencyEnvironment();

			IsolateServer server = new IsolateServer() {
				{
					power = 4;
					slowPart = 0;

					setCpuCost(IsolateServer.READ_DATA, 30);
					setCpuCost(IsolateServer.WRITE_DATA, 35);
				}
			};
			server.setStorage(storage);
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

						}
					});

			env.run(86400l);

			System.out.println(clientCount + "\t" + all.getAverage() + "\t"
					+ all.getCount() + "\t" + read.getAverage() + "\t"
					+ write.getAverage());
		}
	}
}
