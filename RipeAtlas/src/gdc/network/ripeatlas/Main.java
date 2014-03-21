package gdc.network.ripeatlas;

import gdc.network.ripeatlas.api.Service;
import gdc.network.ripeatlas.model.ProbeSpec;
import gdc.network.ripeatlas.model.TracerouteCreate;

public class Main {

	public static void main(String[] args) throws Exception {

		Service service = new Service();

		TracerouteCreate tc = new TracerouteCreate();
		tc.setAf(4);
		tc.setDescription("API created Measurement");
		tc.setOneoff(true);
		tc.setTarget("www.clarkson.edu");
		tc.setProtocol("TCP");
		tc.setType("traceroute");

		ProbeSpec probespec = new ProbeSpec();
		probespec.setRequested(1);
		probespec.setType(ProbeSpec.Type.country.name());
		probespec.setValue("CA");

		service.measurements().create(tc, probespec).execute();
	}
}
