package edu.clarkson.gdc.simulator.impl.tact;

public class Timestamp implements Comparable<Timestamp> {

	private long time;

	private int serverNum;

	public Timestamp(long counter, int number) {
		this.time = counter;
		this.serverNum = number;
	}

	@Override
	public int compareTo(Timestamp another) {
		int compare = this.getTime().compareTo(another.getTime());
		if (compare != 0)
			return compare;
		return getServerNum().compareTo(another.getServerNum());
	}

	public Long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Integer getServerNum() {
		return serverNum;
	}

	public void setServerNum(int serverNum) {
		this.serverNum = serverNum;
	}

}
