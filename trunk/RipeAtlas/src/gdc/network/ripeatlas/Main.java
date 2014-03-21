package gdc.network.ripeatlas;

import gdc.network.ripeatlas.api.Service;

public class Main {

	public static void main(String[] args) throws Exception {

		Service service = new Service();
		System.out.println(service.probes().list(0, 20, (Object[]) null)
				.execute().getResult().size());
	}

}
