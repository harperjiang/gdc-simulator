package gdc.network.ripeatlas.snippet;

import gdc.network.ripeatlas.api.Service;
import gdc.network.ripeatlas.model.MeasurementCreate;
import gdc.network.ripeatlas.model.Probe;
import gdc.network.ripeatlas.model.ProbeSpec;
import gdc.network.ripeatlas.model.TracerouteTarget;

import java.util.ArrayList;
import java.util.List;

public class CreateMeasurement {

	public static void main(String[] args) throws Exception {
		Service service = new Service();

		MeasurementCreate mc = new MeasurementCreate();

		TracerouteTarget tt = new TracerouteTarget();
		tt.setOneoff(true);
		tt.setAf(4);
		tt.setTarget("www.clarkson.edu");
		tt.setDescription("Test Measurement Create");
		tt.setProtocol("TCP");
		tt.setResolveOnProbe(true);
		tt.setType("traceroute");

		mc.getTargets().add(tt);

		ProbeSpec ps = new ProbeSpec();
		ps.setRequested(3);
		ps.setType("probes");
		
		StringBuilder sb = new StringBuilder();
		sb.append(6018).append(",");
		sb.append(6019).append(",");
		sb.append(17);
		ps.setValue(sb.toString());

		mc.getProbes().add(ps);

		service.measurements().create(mc).execute();

	}

}
