package edu.clarkson.gdc.simulator.impl.tact.message;

import java.util.List;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.impl.tact.Operation;

public class ServerPush extends DataMessage {

	private int serverNum;

	public int getServerNum() {
		return serverNum;
	}

	public void setServerNum(int serverNum) {
		this.serverNum = serverNum;
	}

	/**
	 * The biggest operation time in this push
	 * 
	 * @return
	 */
	public Long getEndPoint() {
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
