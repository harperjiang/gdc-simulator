package edu.clarkson.gdc.simulator.scenario.latency.simple.client;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

import edu.clarkson.gdc.simulator.framework.utils.FileCursor;
import edu.clarkson.gdc.simulator.framework.utils.FileCursor.LineProcessor;
import edu.clarkson.gdc.simulator.scenario.latency.simple.WorkloadProvider;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class DefaultWorkloadProvider implements WorkloadProvider {

	public static final class Workload implements Comparable<Workload> {

		private long tick;

		private List<String> workload;

		public Workload(long tick, List<String> workload) {
			this.tick = tick;
			this.workload = workload;
		}

		public long getTick() {
			return tick;
		}

		public List<String> getWorkload() {
			return workload;
		}

		@Override
		public int compareTo(Workload o) {
			Workload wl1 = this;
			Workload wl2 = o;
			if (wl1.getTick() == wl2.getTick())
				return 0;
			return wl1.getTick() > wl2.getTick() ? 1 : -1;
		}
	}

	public DefaultWorkloadProvider() {
		super();
		workloads = new PriorityQueue<Workload>();
	}

	private PriorityQueue<Workload> workloads;

	private int loadcount = 500;

	@Override
	public List<String> fetchReadLoad(long tick) {
		List<String> keys = new ArrayList<String>();
		Workload workload = null;
		while ((workload = peekQueue()) != null && workload.getTick() <= tick) {
			pollQueue();
			keys.addAll(workload.getWorkload());
		}
		return keys;
	}

	protected Workload peekQueue() {
		if (workloads.isEmpty())
			for (int i = 0; i < loadcount; i++) {
				Workload workload = fileCursor.next();
				if(null == workload)
					break;
				workloads.offer(workload);
			}
		return workloads.peek();
	}

	protected void pollQueue() {
		workloads.poll();
	}

	private FileCursor<Workload> fileCursor;

	public void load(String fileName, final int unit) {
		// If the file size is big, don't read it to memory
		fileCursor = new FileCursor<Workload>(fileName);
		fileCursor.setLineProcessor(new LineProcessor<Workload>() {
			private long offset = 0;

			@Override
			public Workload process(String line) {
				String[] parts = line.split(",");
				String timestamp = parts[0];
				String keystr = parts[1];
				String[] keys = keystr.split(";");
				List<String> workload = new ArrayList<String>();
				for (String key : keys)
					workload.add(key);
				Date date = df.parse(timestamp, new ParsePosition(0));
				if (0 == offset)
					offset = date.getTime();
				long tick = (date.getTime() - offset) / unit;
				Workload w = new Workload(tick, workload);
				return w;
			}
		});
	}

	static final DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
}
