package edu.clarkson.gdc.simulator.scenario.tact.message;

import edu.clarkson.gdc.simulator.framework.DataMessage;

public class ServerPull extends DataMessage {

	private int serverNum;

	public int getServerNum() {
		return serverNum;
	}

	public void setServerNum(int serverNum) {
		this.serverNum = serverNum;
	}

	private long startPoint;

	public long getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(long startPoint) {
		this.startPoint = startPoint;
	}

}
