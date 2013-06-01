package edu.clarkson.gdc.simulator.scenario.workload.readmywrite;

import java.math.BigDecimal;

import edu.clarkson.gdc.simulator.framework.Environment;
import edu.clarkson.gdc.simulator.framework.FailMessage;
import edu.clarkson.gdc.simulator.framework.NodeMessageEvent;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.ResponseMessage;
import edu.clarkson.gdc.simulator.module.datadist.ConsistentHashingDistribution;
import edu.clarkson.gdc.simulator.module.exstrgy.FailureRateStrategy;
import edu.clarkson.gdc.simulator.module.server.KeyGateway;
import edu.clarkson.gdc.simulator.scenario.TypedAverager;

public class ScenarioWithGateway {
	private BigDecimal result;

	public ScenarioWithGateway(int reqProcessTime, double failureRate,
			double readratio, int interval) {
		Environment env = new Environment();

		WorkloadDataCenter wdc = new WorkloadDataCenter(reqProcessTime);
		wdc.setId("wdc-1");
		wdc.setPower(1);
		wdc.setExceptionStrategy(new FailureRateStrategy(failureRate));
		env.add(wdc);

		KeyGateway kg = new KeyGateway();
		kg.setDistribution(new ConsistentHashingDistribution());
		env.add(kg);

		ReadMyWriteClient wc = new ReadMyWriteClient(0, readratio, interval,
				100);
		env.add(wc);

		wc.addListener(NodeMessageListener.class,
				env.getProbe(NodeMessageListener.class));

		new Pipe(wc, kg);
		new Pipe(kg, wdc);

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

		result = averager.percent("FAIL1");
		if (null == result)
			result = BigDecimal.ZERO;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++)
			System.out
					.println(new ScenarioWithGateway(10, 0.05, 0.5, 20).result);
	}
}
