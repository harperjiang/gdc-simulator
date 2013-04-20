package edu.clarkson.gdc.simulator.framework;

import java.util.List;
import java.util.Map;

public interface ProcessTimeModel {

	public long latency(Component component, Map<Pipe,List<DataMessage>> msgs);

	public static class ConstantTimeModel implements ProcessTimeModel {

		private long latency;

		public ConstantTimeModel(long latency) {
			this.latency = latency;
		}

		@Override
		public long latency(Component component, Map<Pipe,List<DataMessage>> msgs) {
			return latency;
		}
		
		public long getLatency() {
			return latency;
		}
	}
}
