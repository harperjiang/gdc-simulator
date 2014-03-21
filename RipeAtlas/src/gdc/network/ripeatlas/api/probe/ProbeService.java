package gdc.network.ripeatlas.api.probe;

public class ProbeService {

	public ProbeGetRequest get(String id) {
		ProbeGetRequest req = new ProbeGetRequest();
		req.setId(id);
		return req;
	}

	public ProbeListRequest list(int offset, int limit, Object... query) {
		ProbeListRequest request = new ProbeListRequest();
		request.setLimit(limit);
		request.setOffset(offset);
		request.setQuery(query);
		return request;
	}
}
