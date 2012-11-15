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
		listeners = new ArrayList<ClockListener>();
	}

	private List<Stepper> steppers;

	private List<ClockListener> listeners;

	public void register(Stepper stepper) {
		steppers.add(stepper);
	}

	private static Clock instance = new Clock();

	public static Clock getInstance() {
		return instance;
	}

	public void addListener(ClockListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(ClockListener listener) {
		this.listeners.remove(listener);
	}

	protected void stepForward() {
		ClockEvent event = new ClockEvent(this);
		for (ClockListener listener : listeners) {
			listener.stepForward(event);
		}
	}

	public void step() {
		counter++;
		for (Stepper stepper : steppers)
			stepper.send();
		for (Stepper stepper : steppers)
			stepper.process();
		stepForward();
	}
}
