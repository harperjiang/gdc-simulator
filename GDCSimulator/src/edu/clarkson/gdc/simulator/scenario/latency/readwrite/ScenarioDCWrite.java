package edu.clarkson.gdc.simulator.scenario.latency.readwrite;

import java.util.ArrayList;
import java.util.List;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.framework.NodeMessageEvent;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.storage.DefaultCacheStorage;
import edu.clarkson.gdc.simulator.module.message.ClientRead;
import edu.clarkson.gdc.simulator.module.message.ClientResponse;
import edu.clarkson.gdc.simulator.module.server.AbstractDataCenter;
import edu.clarkson.gdc.simulator.module.server.LoadBalancer;
import edu.clarkson.gdc.simulator.module.server.twopc.TwoPCServer;
import edu.clarkson.gdc.simulator.scenario.Averager;

public class ScenarioDCWrite {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DefaultCacheStorage storage = new DefaultCacheStorage();
		storage.setReadTime(50);
		storage.setWriteTime(70);

		for (int index = 1; index < 50; index++) {
			int clientCount = 100;
			int serverCount = index;

			LatencyEnvironment env = new LatencyEnvironment();

			LoadBalancer loadbalancer = new LoadBalancer();
			env.add(loadbalancer);

			List<AbstractDataCenter> servers = new ArrayList<AbstractDataCenter>();
			for (int i = 0; i < serverCount; i++) {
				AbstractDataCenter server = new TwoPCServer() {
					{
						power = 4;
						slowPart = 0;

						setCpuCost(TwoPCServer.READ_DATA, 30);
						setCpuCost(TwoPCServer.WRITE_DATA, 30);
						setCpuCost(TwoPCServer.SEND_VOTE, 30);
						setCpuCost(TwoPCServer.RECEIVE_VOTE, 30);
						setCpuCost(TwoPCServer.SEND_FINALIZE, 30);
						setCpuCost(TwoPCServer.RECEIVE_FINALIZE, 30);
					}
				};
				server.setStorage(storage);
				env.add(server);
				servers.add(server);
				new Pipe(loadbalancer, server);
			}
			for (int i = 0; i < serverCount; i++) {
				for (int j = i + 1; j < serverCount; j++) {
					// Assume a relative high latency between data centers
					new Pipe(servers.get(i), servers.get(j), 20);
				}
			}

			for (int i = 0; i < clientCount; i++) {
				WaitClient client = new WaitClient() {
					{
						readRatio = 0.25f;
					}
				};
				new Pipe(client, loadbalancer);
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

			System.out.println(serverCount + "\t" + all.getAverage() + "\t"
					+ read.getAverage() + "\t" + write.getAverage() + "\t"
					+ read.getCount() + "\t" + write.getCount());
		}
	}

}
