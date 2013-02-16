package edu.clarkson.gdc.dashboard.domain.entity;

import java.math.BigDecimal;

public class NodeStatus {

	private Node node;

	private String dataType;

	private BigDecimal value;

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
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
