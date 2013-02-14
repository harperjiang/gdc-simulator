package edu.clarkson.gdc.dashboard.entity;

import java.util.ArrayList;
import java.util.List;

public class DataCenter extends Node {

	private List<Battery> batteries;

	public DataCenter() {
		batteries = new ArrayList<Battery>();
	}

	public List<Battery> getBatteries() {
		return batteries;
	}

}
