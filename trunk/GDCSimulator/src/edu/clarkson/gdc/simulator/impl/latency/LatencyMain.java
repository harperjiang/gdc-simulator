package edu.clarkson.gdc.simulator.impl.latency;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.framework.NodeMessageEvent;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.Pipe;

public class LatencyMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int clientCount = 22;

		LatencyEnvironment env = new LatencyEnvironment();

		LatencyServer server = new LatencyServer() {
			{
				power = 10;
			}
		};
		env.add(server);

		for (int i = 0; i < clientCount; i++) {
			LatencyClient client = new LatencyClient();
			new Pipe(client, server);
			env.add(client);
			client.addListener(NodeMessageListener.class,
					env.getProbe(NodeMessageListener.class));
		}

		final AtomicInteger messageCount = new AtomicInteger(0);

		final AtomicLong timeCount = new AtomicLong(0l);

		env.addListener(NodeMessageListener.class, new NodeMessageListener() {

			@Override
			public void messageReceived(NodeMessageEvent event) {
				if (event.getSource() instanceof Client
						&& event.getMessage() instanceof LatencyResponse) {
					messageCount.incrementAndGet();

					LatencyResponse resp = (LatencyResponse) event.getMessage();
					timeCount.addAndGet(resp.getReceiveTime()
							- resp.getRequest().getSendTime());
				}
			}

			@Override
			public void messageSent(NodeMessageEvent event) {

			}
		});

		env.run(86400l);

		System.out.println(messageCount.get());
		System.out.println(timeCount.get() / messageCount.get());
	}

}
