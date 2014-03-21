package gdc.network.ripeatlas.api.measurement;

import gdc.network.ripeatlas.model.MeasurementCreate;
import gdc.network.ripeatlas.model.ProbeSpec;

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

	public MeasurementCreateRequest create(MeasurementCreate m,
			ProbeSpec... probes) {
		MeasurementCreateRequest mcr = new MeasurementCreateRequest();
		mcr.setMeasurement(m);
		for (ProbeSpec probe : probes)
			mcr.getProbes().add(probe);
		return mcr;
	}
}
