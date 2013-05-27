package edu.clarkson.gdc.simulator.scenario.latency.tact;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.framework.NodeMessageEvent;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.ProcessTimeModel.ConstantTimeModel;
import edu.clarkson.gdc.simulator.framework.storage.DefaultCacheStorage;
import edu.clarkson.gdc.simulator.module.message.KeyRead;
import edu.clarkson.gdc.simulator.module.message.KeyResponse;
import edu.clarkson.gdc.simulator.module.server.AbstractDataCenter;
import edu.clarkson.gdc.simulator.module.server.LoadBalancer;
import edu.clarkson.gdc.simulator.module.server.isolate.IsolateServer;
import edu.clarkson.gdc.simulator.scenario.Averager;

public class ScenarioEventual {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DefaultCacheStorage storage = new DefaultCacheStorage();
		storage.setReadTime(50);
		storage.setWriteTime(70);

		int serverCount = 5;
		int clientCount = 100;

		final TACTEnvironment env = new TACTEnvironment();

		LoadBalancer loadbalancer = new LoadBalancer();
		env.add(loadbalancer);

		AbstractDataCenter[] tdcs = new AbstractDataCenter[serverCount];

		for (int i = 0; i < serverCount; i++) {
			tdcs[i] = new IsolateServer() {
				{
					power = 4;
					slowPart = 0;
					
					setCpuCost(IsolateServer.READ_DATA, 30);
					setCpuCost(IsolateServer.WRITE_DATA, 35);
				}
			};
			tdcs[i].setStorage(storage);
			env.add(tdcs[i]);
			new Pipe(tdcs[i], loadbalancer);
		}

		// Create pipes between data centers
		for (int i = 0; i < serverCount; i++) {
			for (int j = i + 1; j < serverCount; j++) {
				Pipe serverNetwork = new Pipe(tdcs[i], tdcs[j]);
				serverNetwork.setTimeModel(new ConstantTimeModel(10));
			}
		}
		// Create clients and connections
		for (int i = 0; i < clientCount; i++) {
			TACTClient client = new TACTClient();
			env.add(client);
			client.addListener(NodeMessageListener.class,
					env.getProbe(NodeMessageListener.class));
			new Pipe(client, loadbalancer);
		}
		final Averager all = new Averager();
		final Averager read = new Averager();
		final Averager write = new Averager();

		env.addListener(NodeMessageListener.class, new NodeMessageListener() {

			@Override
			public void messageReceived(NodeMessageEvent event) {
				if (event.getSource() instanceof Client
						&& event.getMessage() instanceof KeyResponse) {
					KeyResponse resp = (KeyResponse) event.getMessage();
					long time = resp.getReceiveTime()
							- resp.getRequest().getSendTime();
					all.add(time);
					if (resp.getRequest() instanceof KeyRead) {
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

		System.out.println(serverCount + "\t" + all.getAverage() + "\t"
				+ read.getAverage() + "\t" + write.getAverage() + "\t"
				+ read.getCount() + "\t" + write.getCount());
	}
}
