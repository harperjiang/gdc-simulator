package edu.clarkson.gdc.dashboard.domain.entity;

import java.util.ArrayList;
import java.util.List;

public class DataCenter extends Node {

	private List<Battery> batteries;

	private PowerSource powerSource;

	public DataCenter() {
		batteries = new ArrayList<Battery>();
	}

	public List<Battery> getBatteries() {
		return batteries;
	}

	public PowerSource getPowerSource() {
		return powerSource;
	}

	public void setPowerSource(PowerSource powerSource) {
		this.powerSource = powerSource;
	}

	public String getType() {
		return "Data Center";
	}
}
