package edu.clarkson.gdc.dashboard.entity;

import java.util.ArrayList;
import java.util.List;

public class DataCenter extends Node {

	private List<Battery> batteries;

	private List<Machine> machines;

	public DataCenter() {
		batteries = new ArrayList<Battery>();
		machines = new ArrayList<Machine>();
	}

	public List<Battery> getBatteries() {
		return batteries;
	}

	public List<Machine> getMachines() {
		return machines;
	}

}
