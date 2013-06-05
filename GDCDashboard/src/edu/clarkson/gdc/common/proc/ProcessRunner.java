package edu.clarkson.gdc.common.proc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ProcessRunner {

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
		int result = process.waitFor();
		if (null != handler) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = null;
			while (null != (line = br.readLine())) {
				handler.output(line);
			}
		}
		return result;
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

}
