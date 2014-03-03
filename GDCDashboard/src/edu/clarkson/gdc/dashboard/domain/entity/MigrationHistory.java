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

@Entity
@Table(name = "migration_history")
@TableGenerator(name = "migrationHistoryPk", table = "seq_table", pkColumnName = "name", pkColumnValue = "migration_history", valueColumnName = "count")
public class MigrationHistory {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "migrationHistoryPk")
	private int id;

	@Column(name = "from_machine")
	private String fromMachine;

	@Column(name = "to_machine")
	private String toMachine;

	@Column(name = "vm_name")
	private String vmName;

	@Column(name = "begin_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date beginTime;

	@Column(name = "end_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	public MigrationHistory() {
		
	}
	
	public MigrationHistory(MigrationLog copy) {
		this();
		setId(copy.getId());
		setFromMachine(copy.getFromMachine());
		setToMachine(copy.getToMachine());
		setVmName(copy.getVmName());
		setBeginTime(copy.getBeginTime());
		setEndTime(copy.getEndTime());
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFromMachine() {
		return fromMachine;
	}

	public void setFromMachine(String fromMachine) {
		this.fromMachine = fromMachine;
	}

	public String getToMachine() {
		return toMachine;
	}

	public void setToMachine(String toMachine) {
		this.toMachine = toMachine;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
