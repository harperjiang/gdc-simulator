package gdc.network.ripeatlas.api.measurement;

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
}
