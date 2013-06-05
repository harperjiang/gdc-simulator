package edu.clarkson.gdc.common.proc;

import org.junit.Test;

public class ProcessRunnerTest {

	@Test
	public void testRunAndWait() throws Exception {
		ProcessRunner runner = new ProcessRunner("java", "-version");
		runner.setHandler(new OutputHandler() {
			@Override
			public void output(String input) {
				System.out.println(input);
			}
		});
		runner.runAndWait();
	}

}
