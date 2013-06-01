package edu.clarkson.gdc.simulator.scenario;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.simulator.common.Pair;

public class TypedAverager {

	private Map<String, Pair<Long, BigDecimal>> values;

	private Map<String, BigDecimal> percentage;

	private BigDecimal average;

	private Map<String, BigDecimal> averages;

	public static final int SCALE = 4;

	public TypedAverager() {
		super();
		values = new HashMap<String, Pair<Long, BigDecimal>>();

		percentage = new HashMap<String, BigDecimal>();
		averages = new HashMap<String, BigDecimal>();
	}

	public void add(String type, BigDecimal value) {
		Validate.notEmpty(type);
		Validate.notNull(value);
		if (!values.containsKey(type)) {
			values.put(type, new Pair<Long, BigDecimal>(0l, BigDecimal.ZERO));
		}
		Pair<Long, BigDecimal> current = values.get(type);
		values.put(type, new Pair<Long, BigDecimal>(current.getA() + 1, current
				.getB().add(value)));
	}

	public void start() {
		values.clear();
	}

	public void stop() {
		percentage.clear();
		averages.clear();

		Long total = 0l;
		BigDecimal sum = BigDecimal.ZERO;
		for (Entry<String, Pair<Long, BigDecimal>> entry : values.entrySet()) {
			String key = entry.getKey();
			Pair<Long, BigDecimal> val = entry.getValue();
			if (val.getA() == 0)
				averages.put(key, BigDecimal.ZERO);
			else
				averages.put(
						key,
						val.getB().divide(BigDecimal.valueOf(val.getA()),
								SCALE, BigDecimal.ROUND_HALF_UP));
			total = total + val.getA();
			sum = sum.add(val.getB());
		}
		Validate.isTrue(0 != total, "No result received");
		average = sum.divide(BigDecimal.valueOf(total), SCALE,
				BigDecimal.ROUND_HALF_UP);
		BigDecimal totalBD = BigDecimal.valueOf(total);
		for (Entry<String, Pair<Long, BigDecimal>> entry : values.entrySet()) {
			String key = entry.getKey();
			Pair<Long, BigDecimal> val = entry.getValue();
			percentage.put(
					key,
					BigDecimal.valueOf(val.getA()).divide(totalBD, SCALE,
							BigDecimal.ROUND_HALF_UP));
		}
	}

	public BigDecimal percent(String type) {
		Validate.notEmpty(type);
		return percentage.get(type);
	}

	public BigDecimal average(String type) {
		if (StringUtils.isEmpty(type)) {
			return average;
		}
		return averages.get(type);
	}
}
