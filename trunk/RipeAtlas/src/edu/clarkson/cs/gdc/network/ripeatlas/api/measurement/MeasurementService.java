package edu.clarkson.cs.gdc.network.ripeatlas.api.measurement;

import edu.clarkson.cs.gdc.network.ripeatlas.model.MeasurementCreate;

public class MeasurementService {

	public MeasurementService() {
		super();
	}

	public MeasurementGetRequest get(String id) {
		MeasurementGetRequest req = new MeasurementGetRequest();
		req.setMeasurementId(id);
		return req;
	}

	public MeasurementResultRequest result(String id) {
		MeasurementResultRequest mrr = new MeasurementResultRequest();
		mrr.setMeasurementId(id);
		return mrr;
	}

	public MeasurementCreateRequest create(MeasurementCreate m) {
		MeasurementCreateRequest mcr = new MeasurementCreateRequest();
		mcr.setMeasurement(m);
		return mcr;
	}
}
