package edu.clarkson.gdc.simulator.scenario.latency.tact.message;

import java.util.List;

import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.scenario.latency.tact.Operation;

public class ServerPush extends DataMessage {

	private long endPoint;

	private int serverNum;

	public int getServerNum() {
		return serverNum;
	}

	public void setServerNum(int serverNum) {
		this.serverNum = serverNum;
	}

	public long getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(long endPoint) {
		this.endPoint = endPoint;
	}

	private List<Operation> operations;

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		Validate.notEmpty(operations);
		this.operations = operations;
	}

}
