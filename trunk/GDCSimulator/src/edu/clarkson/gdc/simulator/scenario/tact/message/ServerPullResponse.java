package edu.clarkson.gdc.simulator.scenario.tact.message;

import java.util.List;

import edu.clarkson.gdc.simulator.framework.ResponseMessage;
import edu.clarkson.gdc.simulator.scenario.tact.Operation;

public class ServerPullResponse extends ResponseMessage {

	public ServerPullResponse(ServerPull request) {
		super(request);
	}

	private int serverNum;

	private long endPoint;

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
		this.operations = operations;
	}
}
