package edu.clarkson.gdc.simulator.scenario.simple.client;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import edu.clarkson.gdc.simulator.framework.Clock;
import edu.clarkson.gdc.simulator.scenario.simple.client.DefaultWorkloadProvider;

public class DefaultWorkloadProviderTest {

	@Test
	public void testFetchReadLoad() {
		DefaultWorkloadProvider dwlp = new DefaultWorkloadProvider();
		dwlp.load("test_workload", 10);
		List<String> value = dwlp.fetchReadLoad(0);
		assertEquals(4, value.size());

		int counter = 0;
		while ((value = dwlp.fetchReadLoad(counter)) == null
				|| value.size() == 0) {
			counter++;
		}
		assertEquals(7, value.size());

		int count = 0;
		while (true) {
			value = dwlp.fetchReadLoad(counter);
			if (value != null && value.size() != 0)
				count++;
			if (count > 500)
				break;
			counter++;
		}
	}

	@Test
	public void testLoad() {
		DefaultWorkloadProvider dwlp = new DefaultWorkloadProvider();
		dwlp.load("test_workload", 1000);
	}

}
