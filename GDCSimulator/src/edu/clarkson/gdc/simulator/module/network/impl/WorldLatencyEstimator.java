package edu.clarkson.gdc.simulator.module.network.impl;

public class WorldLatencyEstimator extends GeoLatencyEstimator {

	public WorldLatencyEstimator() {
		super();

		GeoRegion na = new GeoRegion("NORTH_AMERICA", new LatencyCable(25, 125));
		GeoRegion sa = new GeoRegion("SOUTH_AMERICA", new LatencyCable(80, 150));
		GeoRegion eu = new GeoRegion("EUROPE", new LatencyCable(40, 130));
		GeoRegion naf = new GeoRegion("NORTH_AFRICA", new LatencyCable(80, 100));
		GeoRegion ind = new GeoRegion("INDIA", new LatencyCable(50, 150));
		GeoRegion oc = new GeoRegion("OCEANIA", new LatencyCable(50, 100));
		GeoRegion cn = new GeoRegion("CHINA", new LatencyCable(60, 200));
		GeoRegion jp = new GeoRegion("JAPAN", new LatencyCable(50, 80));

		addRegion(na);
		addRegion(sa);
		addRegion(eu);
		addRegion(naf);
		addRegion(ind);
		addRegion(oc);
		addRegion(cn);
		addRegion(jp);

		addConnection(new RegionConnection(na, sa, new LatencyCable(110, 120)));
		addConnection(new RegionConnection(na, eu, new LatencyCable(90, 160)));
		addConnection(new RegionConnection(na, jp, new LatencyCable(120, 130)));
		addConnection(new RegionConnection(na, cn, new LatencyCable(150, 160)));
		addConnection(new RegionConnection(na, oc, new LatencyCable(140, 170)));
		addConnection(new RegionConnection(na, ind, new LatencyCable(245, 277)));
		addConnection(new RegionConnection(cn, eu, new LatencyCable(175, 175)));
		addConnection(new RegionConnection(cn, jp, new LatencyCable(112, 120)));
		addConnection(new RegionConnection(cn, oc, new LatencyCable(140, 180)));
		addConnection(new RegionConnection(cn, ind, new LatencyCable(350, 380)));
		addConnection(new RegionConnection(eu, jp, new LatencyCable(195, 195)));
		addConnection(new RegionConnection(eu, ind, new LatencyCable(121, 145)));
		addConnection(new RegionConnection(jp, ind, new LatencyCable(120, 150)));
	}
}
