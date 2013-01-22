package edu.clarkson.gdc.simulator.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.LoggerFactory;

/**
 * <code>Clock</code> is the central driver in our clock-based step-forward
 * simulator. <code>Clock</code> will send out signal to represent time tick.
 * Each part of the simulator will act one step only when they receive a new
 * signal.
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 */
public class Clock {

	private long counter;

	public long getCounter() {
		return counter;
	}

	public Clock() {
		super();
		steppers = new ArrayList<Stepper>();
		listeners = new ArrayList<ClockListener>();
	}

	private List<Stepper> steppers;

	private List<ClockListener> listeners;

	public void register(Stepper stepper) {
		steppers.add(stepper);
	}

	public void unregister(Stepper stepper) {
		steppers.remove(stepper);
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

		// Use a ThreadPool to concurrently execute them makes things even
		// slower
		/*
		 * List<Callable<Object>> tasks = new ArrayList<Callable<Object>>(); for
		 * (Stepper stepper : steppers) tasks.add(new SendTask(stepper)); try {
		 * threadPool.invokeAll(tasks); } catch (InterruptedException e) {
		 * LoggerFactory.getLogger(getClass()).error(
		 * "Task was interrupted on send phase", e); } tasks.clear(); for
		 * (Stepper stepper : steppers) tasks.add(new ProcessTask(stepper)); try
		 * { threadPool.invokeAll(tasks); } catch (InterruptedException e) {
		 * LoggerFactory.getLogger(getClass()).error(
		 * "Task was interrupted on process phase", e); }
		 */
		for (Stepper stepper : steppers) {
			stepper.send();
		}
		for (Stepper stepper : steppers) {
			stepper.process();
		}
		stepForward();
	}

	private ExecutorService threadPool = Executors.newFixedThreadPool(100,
			new ThreadFactory() {

				int count = 0;

				@Override
				public Thread newThread(Runnable r) {
					Thread thread = new Thread(r);
					thread.setDaemon(true);
					thread.setName("Clock-ThreadPool-" + count++);
					return thread;
				}

			});

	static class SendTask implements Callable<Object> {
		private Stepper stepper;

		public SendTask(Stepper stepper) {
			this.stepper = stepper;
		}

		@Override
		public Object call() throws Exception {
			try {
				stepper.send();
			} catch (RuntimeException e) {
				LoggerFactory.getLogger(getClass()).error(
						"Error while executing send in thread pool", e);
				throw e;
			}

			return null;
		}
	}

	static class ProcessTask implements Callable<Object> {
		private Stepper stepper;

		public ProcessTask(Stepper stepper) {
			this.stepper = stepper;
		}

		@Override
		public Object call() throws Exception {
			try {
				stepper.process();
			} catch (RuntimeException e) {
				LoggerFactory.getLogger(getClass()).error(
						"Error while executing process in thread pool", e);
				throw e;
			}
			return null;
		}
	}
}
