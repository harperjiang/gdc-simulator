package gdc.network.ripeatlas.model;

import java.util.ArrayList;
import java.util.List;

public class TracerouteOutput extends Output {

	private int hop;

	private List<TracerouteData> data;

	public TracerouteOutput() {
		super();
		data = new ArrayList<TracerouteData>();
	}

	public int getHop() {
		return hop;
	}

	public void setHop(int hop) {
		this.hop = hop;
	}

	public List<TracerouteData> getData() {
		return data;
	}

	public void setData(List<TracerouteData> data) {
		this.data = data;
	}

	public static final class TracerouteData {

		private String from;

		private Double roundTripTime;

		private int size;

		private int timeToLive;

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public Double getRoundTripTime() {
			return roundTripTime;
		}

		public void setRoundTripTime(Double roundTripTime) {
			this.roundTripTime = roundTripTime;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public int getTimeToLive() {
			return timeToLive;
		}

		public void setTimeToLive(int timeToLive) {
			this.timeToLive = timeToLive;
		}

	}
}
