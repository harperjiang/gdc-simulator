package edu.clarkson.gdc.dashboard.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

public class NodeHistory {

	private String nodeId;

	private Date time;

	private String dataType;

	private BigDecimal data;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
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
