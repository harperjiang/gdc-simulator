package edu.clarkson.gdc.simulator.scenario.workload.readmywrite;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Map.Entry;
import java.util.Set;

import edu.clarkson.gdc.simulator.framework.Environment;
import edu.clarkson.gdc.simulator.framework.FailMessage;
import edu.clarkson.gdc.simulator.framework.NodeMessageEvent;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.ResponseMessage;
import edu.clarkson.gdc.simulator.framework.storage.DefaultCacheStorage;
import edu.clarkson.gdc.simulator.module.datadist.ConsistentHashingDistribution;
import edu.clarkson.gdc.simulator.module.exstrgy.FailureRateStrategy;
import edu.clarkson.gdc.simulator.module.server.KeyGateway;
import edu.clarkson.gdc.simulator.scenario.TypedAverager;

public class ScenarioReadMyWrite {

	private BigDecimal result;

	public ScenarioReadMyWrite(int wdcCount, int wcCount, int storageSize,
			int reqProcessTime, double failureRate, double readratio,
			int interval, int writesize, int writecpy) {
		Environment env = new Environment();

		KeyGateway gw = new KeyGateway();
		ConsistentHashingDistribution dist = new ConsistentHashingDistribution();
		dist.setWritecopy(writecpy);
		gw.setDistribution(dist);
		gw.setWriteCopy(writecpy);
		env.add(gw);

		for (int i = 0; i < wdcCount; i++) {
			WorkloadDataCenter wdc = new WorkloadDataCenter(reqProcessTime);
			wdc.setId("wdc-" + i);
			wdc.setPower(1);
			wdc.setExceptionStrategy(new FailureRateStrategy(failureRate));
			env.add(wdc);
			// Creating Storage for WDC
			DefaultCacheStorage storage = new DefaultCacheStorage();
			storage.setSize(storageSize);
			wdc.setStorage(storage);

			new Pipe(gw, wdc);
		}

		for (int i = 0; i < wcCount; i++) {
			ReadMyWriteClient wc = new ReadMyWriteClient(i, readratio,
					interval, writesize);
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

		env.run(864000l);
		averager.stop();

		for (Entry<String, Set<String>> entry : gw.getKeyDistribution()
				.entrySet()) {
			System.out.println(MessageFormat.format("{0}:{1}", entry.getKey(),
					entry.getValue().size()));
		}

		result = averager.percent("FAIL1");
		if (null == result)
			result = BigDecimal.ZERO;
	}

	public static void main(String[] args) throws Exception {
		for (int k = 0; k < 10; k++) {
			PrintWriter pw = new PrintWriter(new FileOutputStream(
					"/home/harper/plot" + k));
			for (int i = 1; i < 20; i++) {
				pw.println(MessageFormat.format("{0}\t{1}", i,
						new ScenarioReadMyWrite(10, 50, 1000, 10, 0.05,
								0.75, 20, 100, i).result));
			}
			pw.close();
		}
	}
}
