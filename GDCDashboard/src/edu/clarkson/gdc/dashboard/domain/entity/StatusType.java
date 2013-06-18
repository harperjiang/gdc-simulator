package edu.clarkson.gdc.dashboard.domain.entity;

import edu.clarkson.gdc.common.BooleanConverter;
import edu.clarkson.gdc.common.Converter;

public enum StatusType {

	COMMON_RUNNING("status", new BooleanConverter()), DC_HEALTH("s_health", null), DC_CAPACITY(
			"s_capacity", null), DC_POWER("s_power", null), POWER_POWER("s_power",
			null), POWER_VOLTAGE("s_voltage", null), POWER_TEMPERATURE(
			"s_temperature", null), MACHINE_CPU("s_cpu", null), MACHINE_MEMORY(
			"s_memory", null);

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
