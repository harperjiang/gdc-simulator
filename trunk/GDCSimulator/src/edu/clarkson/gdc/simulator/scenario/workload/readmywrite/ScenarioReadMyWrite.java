package edu.clarkson.gdc.simulator.scenario.workload.readmywrite;

import java.math.BigDecimal;

import edu.clarkson.gdc.simulator.framework.Environment;
import edu.clarkson.gdc.simulator.framework.FailMessage;
import edu.clarkson.gdc.simulator.framework.NodeMessageEvent;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.ResponseMessage;
import edu.clarkson.gdc.simulator.framework.storage.DefaultCacheStorage;
import edu.clarkson.gdc.simulator.module.exstrgy.FailureRateStrategy;
import edu.clarkson.gdc.simulator.module.server.Gateway;
import edu.clarkson.gdc.simulator.scenario.TypedAverager;

public class ScenarioReadMyWrite {

	public ScenarioReadMyWrite(int wdcCount, int wcCount, int storageSize,
			int reqProcessTime, double failureRate, double readratio,
			int interval) {
		Environment env = new Environment();

		KeyGateway gw = new KeyGateway();
		env.add(gw);

		for (int i = 0; i < wdcCount; i++) {
			WorkloadDataCenter wdc = new WorkloadDataCenter(reqProcessTime);
			wdc.setPower(5);
			wdc.setExceptionStrategy(new FailureRateStrategy(failureRate));
			env.add(wdc);
			// Creating Storage for WDC
			DefaultCacheStorage storage = new DefaultCacheStorage();
			storage.setSize(storageSize);
			wdc.setStorage(storage);

			new Pipe(gw, wdc);
		}

		for (int i = 0; i < wcCount; i++) {
			ReadMyWriteClient wc = new ReadMyWriteClient(i, readratio, interval);
			env.add(wc);
			wc.addListener(NodeMessageListener.class,
					env.getProbe(NodeMessageListener.class));
			new Pipe(wc, gw);
		}

		final TypedAverager averager = new TypedAverager();
		averager.start();
		env.addListener(NodeMessageListener.class, new NodeMessageListener() {
			@Override
			public void messageReceived(NodeMessageEvent event) {
				ResponseMessage resp = (ResponseMessage) event.getMessage();
				if (resp instanceof FailMessage) {
					averager.add("FAIL1", BigDecimal.ZERO);
				} else {
					averager.add("SUCCESS", new BigDecimal(resp.getSendTime()
							- resp.getRequest().getReceiveTime()));
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
		averager.stop();

		System.out.println(averager.average("SUCCESS"));
		System.out.println(averager.percent("FAIL1"));
	}

	public static void main(String[] args) {
		new ScenarioReadMyWrite();
	}
}
