package edu.clarkson.gdc.common;

public class BooleanConverter implements Converter<Boolean> {

	@Override
	public Boolean convert(String val) {
		return Boolean.valueOf(val);
	}
}
