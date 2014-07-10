package edu.clarkson.cs.gdc.network.ripeatlas.model;

import edu.clarkson.cs.gdc.network.ripeatlas.api.common.deserializer.JsonAttribute;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class MeasurementCreate {

	private Date startTime;

	private Date stopTime;

	@JsonAttribute("definitions")
	private List<MeasurementTarget> targets;

	private List<ProbeSpec> probes;

	public MeasurementCreate() {
		super();
		targets = new ArrayList<MeasurementTarget>();
		probes = new ArrayList<ProbeSpec>();
	}

	public List<MeasurementTarget> getTargets() {
		return targets;
	}

	public List<ProbeSpec> getProbes() {
		return probes;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public void setTargets(List<MeasurementTarget> targets) {
		this.targets = targets;
	}

	public void setProbes(List<ProbeSpec> probes) {
		this.probes = probes;
	}

}
