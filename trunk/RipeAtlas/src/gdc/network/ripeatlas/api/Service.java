package gdc.network.ripeatlas.api;

import gdc.network.ripeatlas.api.measurement.MeasurementService;
import gdc.network.ripeatlas.api.probe.ProbeService;

public class Service {

	public Service() {
		super();
	}

	public MeasurementService measurements() {
		return new MeasurementService();
	}

	public ProbeService probes() {
		return new ProbeService();
	}
}
