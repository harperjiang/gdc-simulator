package edu.clarkson.gdc.simulator.scenario.latency.readwrite;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.time.StopWatch;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.framework.NodeMessageEvent;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.storage.DefaultCacheStorage;
import edu.clarkson.gdc.simulator.module.message.KeyResponse;
import edu.clarkson.gdc.simulator.module.server.LoadBalancer;
import edu.clarkson.gdc.simulator.module.server.isolate.IsolateServer;

public class ScenarioMultiDCRead {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DefaultCacheStorage storage = new DefaultCacheStorage();
		storage.setReadTime(50);
		storage.setWriteTime(70);
		for (int serverCount = 1; serverCount < 20; serverCount++) {
			int clientCount = 100;

			LatencyEnvironment env = new LatencyEnvironment();

			LoadBalancer loadbalancer = new LoadBalancer();
			env.add(loadbalancer);

			for (int i = 0; i < serverCount; i++) {
				IsolateServer server = new IsolateServer() {
					{
						power = 4;
						slowPart = 0;

						setCpuCost(IsolateServer.READ_DATA, 30);
						setCpuCost(IsolateServer.WRITE_DATA, 35);
					}
				};
				server.setStorage(storage);
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
									&& event.getMessage() instanceof KeyResponse) {
								KeyResponse resp = (KeyResponse) event
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

			StopWatch watch = new StopWatch();
			watch.start();
			env.run(864000l);
			watch.stop();
			System.out.println(serverCount + "\t" + timeCount.get()
					/ messageCount.get() + "\t" + messageCount.get());
			System.out.println(watch.getTime());
		}
	}
}
