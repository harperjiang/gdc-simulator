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
@Table(name = "alert")
public class Alert {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "alertPk")
	@Column(name = "id")
	private long id;

	@Column(name = "level")
	private String level;

	@Column(name = "node_id")
	private String nodeId;

	@Column(name = "time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	@Column(name = "type")
	private String type;

	@Column(name = "description")
	private String description;

	@Column(name = "node_name")
	private String nodeName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AlertLevel getLevel() {
		return AlertLevel.valueOf(level);
	}

	public void setLevel(AlertLevel level) {
		this.level = level.name();
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public static boolean isAlert(String string) {
		try {
			AlertType.valueOf(string);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
