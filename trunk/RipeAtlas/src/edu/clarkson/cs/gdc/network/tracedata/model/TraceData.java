package edu.clarkson.cs.gdc.network.tracedata.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "trace_data")
public class TraceData {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "source_ip")
	private String sourceIp;

	@Column(name = "from_ip")
	private String fromIp;

	@Column(name = "to_ip")
	private String toIp;

	@Column(name = "rtt")
	private BigDecimal rtt;

	@Column(name = "rtt_source")
	private BigDecimal rttSource;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public String getFromIp() {
		return fromIp;
	}

	public void setFromIp(String fromIp) {
		this.fromIp = fromIp;
	}

	public String getToIp() {
		return toIp;
	}

	public void setToIp(String toIp) {
		this.toIp = toIp;
	}

	public BigDecimal getRtt() {
		return rtt;
	}

	public void setRtt(BigDecimal rtt) {
		this.rtt = rtt;
	}

	public BigDecimal getRttSource() {
		return rttSource;
	}

	public void setRttSource(BigDecimal rttSource) {
		this.rttSource = rttSource;
	}

}
