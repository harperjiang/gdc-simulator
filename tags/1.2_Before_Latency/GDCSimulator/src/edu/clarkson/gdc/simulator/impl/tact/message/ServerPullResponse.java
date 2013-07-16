package edu.clarkson.gdc.simulator.impl.tact.message;

import java.util.List;

import edu.clarkson.gdc.simulator.framework.ResponseMessage;
import edu.clarkson.gdc.simulator.impl.tact.Operation;

public class ServerPullResponse extends ResponseMessage {

	public ServerPullResponse(ServerPull request) {
		super(request);
	}

	private int serverNum;

	public int getServerNum() {
		return serverNum;
	}

	public void setServerNum(int serverNum) {
		this.serverNum = serverNum;
	}

	public long getEndPoint() {
		if (null == operations || operations.size() == 0)
			return 0;
		return operations.get(operations.size() - 1).getTimestamp().getTime();
	}

	private List<Operation> operations;

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}
}