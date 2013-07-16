package edu.clarkson.gdc.dashboard.domain.entity;

import edu.clarkson.gdc.common.BooleanConverter;
import edu.clarkson.gdc.common.Converter;

public enum StatusType {

	STATUS(new BooleanConverter()), 
	SUMMARY_HEALTH(null), SUMMARY_UTIL(null), SUMMARY_VMRUNNING(null),
	DC_HEALTH(null), DC_GENERATION(null), DC_CONSUMPTION(null), 
	UPS_POWER(null), UPS_VOLTAGE(null), UPS_TEMPERATURE(null), 
	POWER_INPUT_I(null), POWER_BTY_I(null), POWER_INVRT_I(null), POWER_BTY_V(null), POWER_CHARGE(null), 
	MACHINE_CPU(null), MACHINE_MEMORY(null), MACHINE_VMCOUNT(null),
	BTY_LEVEL(null);
	
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
