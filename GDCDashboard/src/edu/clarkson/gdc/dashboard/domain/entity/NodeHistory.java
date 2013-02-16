package edu.clarkson.gdc.dashboard.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

public class NodeHistory {

	private Node node;

	private Date time;

	private String dataType;

	private BigDecimal data;

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public BigDecimal getData() {
		return data;
	}

	public void setData(BigDecimal data) {
		this.data = data;
	}

}
