package edu.clarkson.gdc.simulator.scenario.latency.solar;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Map.Entry;

import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.module.client.RandomClient;

public class SolarClient extends RandomClient {

	public SolarClient() {
		waitResponse = true;
		// timeout = 2000;
	}

	private Holder[] boundary;

	int pointer = 0;

	@Override
	protected void init() {
		boundary = new Holder[getPipes().size() - 1];
		int counter = 0;
		for (Entry<Node, Pipe> entry : getPipes().entrySet()) {
			if (entry.getKey() instanceof SolarServer) {
				Point2D.Double loc = entry.getKey().getLocation();
				long val = (long) (43200 - loc.y * 240);
				if (val < 0)
					val += 86400;
				boundary[counter++] = new Holder(entry.getValue(), val);
			}
		}
		Arrays.sort(boundary);

		if (distance(0l, boundary[0].noon) <= distance(0l,
				boundary[boundary.length - 1].noon)) {
			pointer = 0;
		} else {
			pointer = boundary.length - 1;
		}
	}

	protected static long distance(long refloc, long noon) {
		long clockwise = refloc - noon;
		long countercw = noon - refloc;
		if (clockwise < 0)
			clockwise += 86400;
		if (countercw < 0)
			countercw += 86400;
		return Math.min(clockwise, countercw);
	}

	@Override
	protected boolean genRead(MessageRecorder recorder) {
		// Send to the most noon
		int nextp = (pointer + 1) % boundary.length;
		long current = getClock().getCounter() / (1000 / TimeConstant.UNIT);
		if (distance(current, boundary[pointer].noon) > distance(current,
				boundary[nextp].noon))
			pointer = nextp;
		SolarClientRead scr = new SolarClientRead();
		scr.setTimeout(2000);// 20s
		recorder.record(boundary[pointer].pipe, scr);
		return true;
	}

	@Override
	protected boolean genWrite(MessageRecorder recorder) {
		return false;
	}

	protected static class Holder implements Comparable<Holder> {

		public Pipe pipe;

		public long noon;

		public Holder(Pipe pipe, long val) {
			this.pipe = pipe;
			this.noon = val;
		}

		@Override
		public int compareTo(Holder another) {
			return Long.valueOf(noon).compareTo(another.noon);
		}
	}
}
