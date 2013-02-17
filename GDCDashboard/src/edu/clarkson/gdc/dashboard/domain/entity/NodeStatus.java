package edu.clarkson.gdc.dashboard.domain.entity;

import java.math.BigDecimal;

public class NodeStatus {

	private String nodeId;

	private String dataType;

	private BigDecimal value;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}
