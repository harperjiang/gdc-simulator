package edu.clarkson.gdc.simulator.scenario.tact;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.framework.NodeMessageEvent;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.ProcessTimeModel.ConstantTimeModel;
import edu.clarkson.gdc.simulator.module.message.ClientRead;
import edu.clarkson.gdc.simulator.module.message.ClientResponse;
import edu.clarkson.gdc.simulator.module.server.LoadBalancer;
import edu.clarkson.gdc.simulator.scenario.Averager;

public class ScenarioTACT {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int orderError = 1; orderError < 100; orderError ++) {
			int serverCount = 5;
			int clientCount = 200;

			final TACTEnvironment env = new TACTEnvironment();

			LoadBalancer loadbalancer = new LoadBalancer();
			env.add(loadbalancer);

			TACTDataCenter[] tdcs = new TACTDataCenter[serverCount];

			for (int i = 0; i < serverCount; i++) {
				tdcs[i] = new TACTDataCenter(i, serverCount) {
					{
						power = 4;
						slowPart = 0;
					}
				};
				tdcs[i].setNumError(1000);
				tdcs[i].setOrderError(orderError);
				tdcs[i].setStaleness(400);
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

			System.out.println(orderError + "\t" + all.getAverage() + "\t"
					+ read.getAverage() + "\t" + write.getAverage() + "\t"
					+ read.getCount() + "\t" + write.getCount());
		}
	}
}
