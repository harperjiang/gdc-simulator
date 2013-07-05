package edu.clarkson.gdc.dashboard.domain.entity;

public enum VMStatus {
	RUNNING("running"), SHUT_OFF("shut off");

	private String value;

	VMStatus(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
