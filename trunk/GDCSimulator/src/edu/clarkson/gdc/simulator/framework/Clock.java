package edu.clarkson.gdc.simulator.framework;

import java.util.ArrayList;
import java.util.List;

public class Clock {

	private long counter;

	public long getCounter() {
		return counter;
	}

	private Clock() {
		super();
		steppers = new ArrayList<Stepper>();
	}

	private List<Stepper> steppers;

	public void register(Stepper stepper) {
		steppers.add(stepper);
	}

	private static Clock instance = new Clock();

	public static Clock getInstance() {
		return instance;
	}

	public void step() {
		counter++;
		for (Stepper stepper : steppers)
			stepper.send();
		for (Stepper stepper : steppers)
			stepper.process();
	}
}
