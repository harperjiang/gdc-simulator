package edu.clarkson.gdc.dashboard.domain.entity;

import edu.clarkson.gdc.common.BooleanConverter;
import edu.clarkson.gdc.common.Converter;

public enum StatusType {

	COMMON_RUNNING("status", new BooleanConverter()), DC_HEALTH("health", null), DC_CAPACITY(
			"capacity", null), DC_POWER("power", null), POWER_POWER("power",
			null), POWER_VOLTAGE("voltage", null), POWER_TEMPERATURE(
			"temperature", null), MACHINE_CPU("cpu", null), MACHINE_MEMORY(
			"memory", null);

	private String key;

	private Converter<?> converter;

	private StatusType(String key, Converter<?> converter) {
		this.key = key;
		this.converter = converter;
	}

	public String key() {
		return this.key;
	}

	public Object convert(String val) {
		if (null == converter)
			return val;
		return this.converter.convert(val);
	}
}
