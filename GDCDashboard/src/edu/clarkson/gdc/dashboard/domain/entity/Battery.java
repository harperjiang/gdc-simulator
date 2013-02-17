package edu.clarkson.gdc.dashboard.domain.entity;

import java.util.ArrayList;
import java.util.List;

public class Battery extends Node {

	private List<Machine> machines;

	public Battery() {
		machines = new ArrayList<Machine>();
	}

	public List<Machine> getMachines() {
		return machines;
	}

	public String getType() {
		return "Battery";
	}
}
