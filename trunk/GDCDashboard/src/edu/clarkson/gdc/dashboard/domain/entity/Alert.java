package edu.clarkson.gdc.dashboard.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@TableGenerator(name = "alertPk", table = "seq_table", pkColumnName = "name", pkColumnValue = "alert", valueColumnName = "count")
@Entity
@Table(name="alert")
public class Alert {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "alertPk")
	@Column(name = "id")
	private long id;

	@Column(name = "level")
	private AlertLevel level;

	@Column(name = "node_id")
	private String nodeId;

	@Column(name = "time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "node_type")
	private String nodeType;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AlertLevel getLevel() {
		return level;
	}

	public void setLevel(AlertLevel level) {
		this.level = level;
	}

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

}
