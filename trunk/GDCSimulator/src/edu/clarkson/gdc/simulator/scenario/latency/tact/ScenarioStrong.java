package edu.clarkson.gdc.simulator.scenario.latency.tact;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

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
import edu.clarkson.gdc.simulator.module.server.twopc.TwoPCServer;
import edu.clarkson.gdc.simulator.scenario.Averager;

public class ScenarioStrong {

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		DefaultCacheStorage storage = new DefaultCacheStorage();
		storage.setReadTime(1);
		storage.setWriteTime(1);

		int serverCount = 5;
		int clientCount = 100;

		final TACTEnvironment env = new TACTEnvironment();

		LoadBalancer loadbalancer = new LoadBalancer();
		env.add(loadbalancer);

		AbstractDataCenter[] tdcs = new AbstractDataCenter[serverCount];

		for (int i = 0; i < serverCount; i++) {
			tdcs[i] = new TwoPCServer() {
				{
					power = 4;
					slowPart = 0;

					setCpuCost(TwoPCServer.READ_DATA, 20);
					setCpuCost(TwoPCServer.WRITE_DATA, 20);
					setCpuCost(TwoPCServer.SEND_VOTE, 20);
					setCpuCost(TwoPCServer.RECEIVE_VOTE, 15);
					setCpuCost(TwoPCServer.SEND_FINALIZE, 15);
					setCpuCost(TwoPCServer.RECEIVE_FINALIZE, 15);
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
		PrintWriter pw = new PrintWriter(new FileOutputStream("tact_strong"));
		pw.println(serverCount + "\t" + all.getAverage() + "\t"
				+ read.getAverage() + "\t" + write.getAverage() + "\t"
				+ read.getCount() + "\t" + write.getCount());
		pw.close();
	}
}
