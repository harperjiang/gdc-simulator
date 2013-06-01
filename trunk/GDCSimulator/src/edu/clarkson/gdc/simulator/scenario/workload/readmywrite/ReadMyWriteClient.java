package edu.clarkson.gdc.simulator.scenario.workload.readmywrite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.clarkson.gdc.simulator.module.client.RandomClient;
import edu.clarkson.gdc.simulator.module.message.KeyRead;
import edu.clarkson.gdc.simulator.module.message.KeyWrite;
import edu.clarkson.gdc.simulator.scenario.latency.simple.DefaultData;

public class ReadMyWriteClient extends RandomClient {

	private int writeSize;

	private double readRatioToBe;
	
	private List<String> written;

	private Random random;

	public ReadMyWriteClient(int index, double readratio, int interval, int ws) {
		super(0, interval, -1, true);
		this.setId(String.valueOf(index));

		this.readRatioToBe = readratio;
		this.writeSize = ws;
		this.written = new ArrayList<String>();

		this.random = new Random(System.currentTimeMillis());
	}

	@Override
	protected boolean genRead(MessageRecorder recorder) {
		if (written.size() == 0)
			return false;
		String key = written.get(random.nextInt(written.size()));
		KeyRead keyRead = new KeyRead();
		keyRead.setKey(key);
		recorder.record(getServerPipe(), keyRead);
		return true;
	}

	@Override
	protected boolean genWrite(MessageRecorder recorder) {
		if(writeSize == written.size()) {
			setReadRatio(readRatioToBe);
		}
		int newindex = random.nextInt(writeSize);
		String key = null;
		if (newindex >= written.size()) {
			// New Key
			key = getId() + "-" + written.size();
			written.add(key);
		} else {
			key = written.get(newindex);
		}

		KeyWrite keyWrite = new KeyWrite(new DefaultData(key));
		recorder.record(getServerPipe(), keyWrite);
		return true;
	}

}
