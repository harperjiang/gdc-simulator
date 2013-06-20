package edu.clarkson.gdc.dashboard.domain.entity;

import edu.clarkson.gdc.common.BooleanConverter;
import edu.clarkson.gdc.common.Converter;

public enum StatusType {

	STATUS(new BooleanConverter()), DC_HEALTH(null), DC_CAPACITY(null), DC_POWER(
			null), POWER_POWER(null), POWER_VOLTAGE(null), POWER_TEMPERATURE(
			null), MACHINE_CPU(null), MACHINE_MEMORY(null);

	private Converter<?> converter;

	private StatusType(Converter<?> converter) {
		this.converter = converter;
	}

	public Object convert(String val) {
		if (null == converter)
			return val;
		return this.converter.convert(val);
	}
}
