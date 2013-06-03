package edu.clarkson.gdc.simulator.scenario.workload.sharepopular;

import java.math.BigDecimal;

import edu.clarkson.gdc.simulator.framework.Environment;
import edu.clarkson.gdc.simulator.framework.FailMessage;
import edu.clarkson.gdc.simulator.framework.NodeMessageEvent;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.ResponseMessage;
import edu.clarkson.gdc.simulator.framework.storage.DefaultCacheStorage;
import edu.clarkson.gdc.simulator.module.datadist.RandomDistribution;
import edu.clarkson.gdc.simulator.module.exstrgy.FailureRateStrategy;
import edu.clarkson.gdc.simulator.module.server.KeyGateway;
import edu.clarkson.gdc.simulator.scenario.TypedAverager;

public class ScenarioSharePopular {

	private BigDecimal result;

	public ScenarioSharePopular(int wdcCount, long processTime,
			double failureRate, int writecpy, long storageSize, int shareCount,
			int readCount) {
		Environment env = new Environment();

		KeyGateway gw = new KeyGateway();
		RandomDistribution dist = new RandomDistribution();
		dist.setWriteCopy(wdcCount);
		gw.setDistribution(dist);
		gw.setWriteCopy(writecpy);
		env.add(gw);

		for (int i = 0; i < wdcCount; i++) {
			WorkloadDataCenter wdc = new WorkloadDataCenter(processTime);
			wdc.setId("wdc-" + i);
			wdc.setPower(1);
			wdc.setExceptionStrategy(new FailureRateStrategy(failureRate));
			env.add(wdc);

			DefaultCacheStorage storage = new DefaultCacheStorage();
			storage.setSize(storageSize);
			wdc.setStorage(storage);

			new Pipe(gw, wdc);
		}

		for (int i = 0; i < shareCount; i++) {
			ShareClient sc = new ShareClient();
			env.add(sc);

			sc.addListener(NodeMessageListener.class,
					env.getProbe(NodeMessageListener.class));
			new Pipe(sc, gw);
		}

		for (int i = 0; i < readCount; i++) {
			ReadClient rc = new ReadClient();
			env.add(rc);

			rc.addListener(NodeMessageListener.class,
					env.getProbe(NodeMessageListener.class));
			new Pipe(rc, gw);
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

		// for (Entry<String, Set<String>> entry : gw.getKeyDistribution()
		// .entrySet()) {
		// System.out.println(MessageFormat.format("{0}:{1}", entry.getKey(),
		// entry.getValue().size()));
		// }

		result = averager.percent("FAIL1");
		if (null == result)
			result = BigDecimal.ZERO;

	}

	public static void main(String[] args) {

	}

}
