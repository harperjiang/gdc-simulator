package edu.clarkson.gdc.simulator.framework;

import java.util.List;
import java.util.Map;

import edu.clarkson.gdc.simulator.framework.ProcessTimeModel.ConstantTimeModel;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 */
public abstract class Component implements Stepper {

	private Environment environment;

	private ProcessTimeModel timeModel = new ConstantTimeModel(1);

	private String id;

	public Component(Environment env) {
		this.environment = env;
		env.add(this);
	}

	public Component() {

	}

	public Component(String id) {
		this();
		setId(id);
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment env) {
		if (null != getEnvironment()) {
			getEnvironment().remove(this);
		}
		this.environment = env;
	}

	public ProcessTimeModel getTimeModel() {
		return timeModel;
	}

	public void setTimeModel(ProcessTimeModel timeModel) {
		this.timeModel = timeModel;
	}

	protected long getLatency(Map<Pipe, List<DataMessage>> msgs) {
		if (null == msgs || msgs.isEmpty() == true)
			return 0;
		if (null == getTimeModel())
			return 1;
		return getTimeModel().latency(this, msgs);
	}

	public Clock getClock() {
		if (null == getEnvironment())
			return null;
		return getEnvironment().getClock();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
