package edu.clarkson.gdc.simulator.scenario.latency.solar2;

public class RegionSolarServer {

	private String regionId;

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	private double sunrise;

	private double sunset;

	public double getSunrise() {
		return sunrise;
	}

	public void setSunrise(double sunrise) {
		this.sunrise = sunrise;
	}

	public double getSunset() {
		return sunset;
	}

	public void setSunset(double sunset) {
		this.sunset = sunset;
	}

	public boolean available(double timepoint) {
		if (sunrise <= sunset)
			return timepoint >= sunrise && timepoint <= sunset;
		else {
			return (timepoint <= sunset && timepoint >= 0)
					|| timepoint >= sunrise;
		}
	}
}
