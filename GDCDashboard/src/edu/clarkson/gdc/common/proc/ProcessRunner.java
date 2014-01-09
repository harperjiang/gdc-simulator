package edu.clarkson.gdc.common.proc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessRunner {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private String[] commands;

	private OutputHandler handler;

	private transient Process process;

	public ProcessRunner(String... commands) {
		this.commands = commands;
	}

	/**
	 * Wait until the process ends
	 * 
	 * @throws InterruptedException
	 */
	public int runAndWait() throws InterruptedException, IOException {

		ProcessBuilder builder = new ProcessBuilder(commands);
		builder.redirectErrorStream(true);

		process = builder.start();
		Thread readThread = new Thread() {
			public void run() {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						process.getInputStream()));
				String line = null;
				try {
					while (null != (line = br.readLine())) {
						if (logger.isDebugEnabled()) {
							logger.debug(line);
						}
						if (null != handler) {
							handler.output(line);
						}
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		};
		readThread.start();
		int result = process.waitFor();
		readThread.join();
		return result;
	}

	public void runLater(final Callback callback) {
		new Thread() {
			public void run() {
				try {
					ProcessRunner.this.runAndWait();
					callback.done();
				} catch (Exception e) {
					logger.error("Exception occurred on asynchronuous task", e);
					callback.exception(e);
				} finally {
					callback.clean();
				}
			}
		}.start();
	}

	public Process getProcess() {
		return process;
	}

	public OutputHandler getHandler() {
		return handler;
	}

	public void setHandler(OutputHandler handler) {
		this.handler = handler;
	}

	public static interface Callback {
		public void done();

		public void exception(Exception e);

		public void clean();
	}
}
