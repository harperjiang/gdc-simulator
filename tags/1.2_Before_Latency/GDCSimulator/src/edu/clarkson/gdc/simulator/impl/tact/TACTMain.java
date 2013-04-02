package edu.clarkson.gdc.simulator.impl.tact;

import java.util.Random;

import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.storage.DefaultCacheStorage;

public class TACTMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int dataCenterCount = 10;
		int clientPerDc = 10;
		int clientPerDcSd = 4;

		Random random = new Random(System.currentTimeMillis());

		TACTEnvironment env = new TACTEnvironment();
		TACTDataCenter[] tdcs = new TACTDataCenter[dataCenterCount];

		DefaultCacheStorage storage = new DefaultCacheStorage();
		storage.setReadTime(100l);
		storage.setWriteTime(500l);

		for (int i = 0; i < dataCenterCount; i++) {
			tdcs[i] = new TACTDataCenter(i, dataCenterCount);
			tdcs[i].setNumError(20);
			tdcs[i].setOrderError(5);
			tdcs[i].setStaleness(1000l);
			tdcs[i].setStorage(storage);
			env.add(tdcs[i]);
		}

		// Create pipes between data centers
		for (int i = 0; i < dataCenterCount; i++) {
			for (int j = i + 1; j < dataCenterCount; j++) {
				new Pipe(tdcs[i], tdcs[j]);
			}
		}
		// Create clients and connections
		for (int i = 0; i < dataCenterCount; i++) {
			int clientCount = clientPerDc + random.nextInt(clientPerDcSd);
			for (int j = 0; j < clientCount; j++) {
				TACTClient client = new TACTClient();
				env.add(client);
				new Pipe(client, tdcs[i]);
			}
		}

		env.run(8640000l);
	}

}
