package edu.clarkson.gdc.simulator.impl.latency;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.framework.NodeMessageEvent;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.impl.LoadBalancer;

public class ScenarioMultiDCRead {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int serverCount = 1; serverCount < 20; serverCount++) {
			int clientCount = 500;

			LatencyEnvironment env = new LatencyEnvironment();

			LoadBalancer loadbalancer = new LoadBalancer();
			env.add(loadbalancer);

			for (int i = 0; i < serverCount; i++) {
				IsolateServer server = new IsolateServer() {
					{
						power = 4;
						slowPart = 0;
					}
				};
				server.setId(String.valueOf(i));
				env.add(server);
				new Pipe(loadbalancer, server);
			}

			for (int i = 0; i < clientCount; i++) {
				WaitClient client = new WaitClient();
				new Pipe(client, loadbalancer);
				env.add(client);
				client.addListener(NodeMessageListener.class,
						env.getProbe(NodeMessageListener.class));
			}

			final AtomicInteger messageCount = new AtomicInteger(0);

			final AtomicLong timeCount = new AtomicLong(0l);

			env.addListener(NodeMessageListener.class,
					new NodeMessageListener() {

						@Override
						public void messageReceived(NodeMessageEvent event) {
							if (event.getSource() instanceof Client
									&& event.getMessage() instanceof ClientResponse) {
								ClientResponse resp = (ClientResponse) event
										.getMessage();
								messageCount.incrementAndGet();
								timeCount.addAndGet(resp.getReceiveTime()
										- resp.getRequest().getSendTime());
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

			System.out.println(serverCount + "\t" + timeCount.get()
					/ messageCount.get() + "\t" + messageCount.get());
		}
	}
}
