package edu.clarkson.gdc.dashboard.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus.NodeStatusId;

@Entity
@Table(name = "node_status")
@IdClass(NodeStatusId.class)
public class NodeStatus {

	public static class NodeStatusId {
		private String nodeId;

		private String dataType;

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

	}

	@Id
	@Column(name = "node_id")
	private String nodeId;

	@Id
	@Column(name = "data_type")
	private String dataType;

	@Column(name = "value")
	private String value;

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public StatusType getType() {
		return StatusType.valueOf(getDataType());
	}
}
