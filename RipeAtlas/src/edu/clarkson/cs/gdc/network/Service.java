package edu.clarkson.cs.gdc.network;

import edu.clarkson.cs.gdc.network.ripeatlas.api.measurement.MeasurementService;
import edu.clarkson.cs.gdc.network.ripeatlas.api.probe.ProbeService;

public class Service {

	private MeasurementService ms;

	private ProbeService ps;

	public Service() {
		super();
		this.ms = new MeasurementService();
		this.ps = new ProbeService();
	}

	public MeasurementService measurements() {
		return ms;
	}

	public ProbeService probes() {
		return ps;
	}
}
