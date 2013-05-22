package edu.clarkson.gdc.simulator.scenario.latency.simple.timemodel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import edu.clarkson.gdc.simulator.framework.Component;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.ProcessTimeModel;

public class SuddenDeathTimeModel implements ProcessTimeModel {

	private BigDecimal power;

	private BigDecimal scale;

	public SuddenDeathTimeModel(BigDecimal power, BigDecimal scale) {
		super();
		this.power = power;
		this.scale = scale;
	}

	public BigDecimal getPower() {
		return power;
	}

	public BigDecimal getScale() {
		return scale;
	}

	@Override
	public long latency(Component component, Map<Pipe, List<DataMessage>> msgs) {
		// TODO Not done yet
		return 0;
	}

}
