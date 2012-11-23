package edu.clarkson.gdc.simulator.impl.client;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import edu.clarkson.gdc.simulator.framework.Clock;

public class DefaultWorkloadProviderTest {

	@Test
	public void testFetchReadLoad() {
		DefaultWorkloadProvider dwlp = new DefaultWorkloadProvider();
		dwlp.load("test_workload", 10);
		List<String> value = dwlp.fetchReadLoad(0);
		assertEquals(4, value.size());

		while ((value = dwlp.fetchReadLoad(Clock.getInstance().getCounter())) == null
				|| value.size() == 0) {
			Clock.getInstance().step();
		}
		assertEquals(7, value.size());

		int count = 0;
		while (true) {
			value = dwlp.fetchReadLoad(Clock.getInstance().getCounter());
			if (value != null && value.size() != 0)
				count++;
			if (count > 500)
				break;
			Clock.getInstance().step();
		}
	}

	@Test
	public void testLoad() {
		DefaultWorkloadProvider dwlp = new DefaultWorkloadProvider();
		dwlp.load("test_workload", 1000);
	}

}
