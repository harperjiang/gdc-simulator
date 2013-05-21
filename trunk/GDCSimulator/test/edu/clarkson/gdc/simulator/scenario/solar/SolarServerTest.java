package edu.clarkson.gdc.simulator.scenario.solar;

import java.awt.geom.Point2D;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;

import org.junit.Test;

import edu.clarkson.gdc.simulator.framework.Environment;
import edu.clarkson.gdc.simulator.scenario.solar.SolarServer;

public class SolarServerTest {

	@Test
	public void testGetPower() throws Exception {
		Environment env = new Environment();
		SolarServer ss = new SolarServer(null, new Point2D.Double(0, 0));
		ss.basepower = 20;
		env.add(ss);
		FileOutputStream fos = new FileOutputStream("solarval");
		PrintWriter pw = new PrintWriter(fos);
		for (int i = 0; i < 86400; i++) {
			env.getClock().tick();
			pw.println(MessageFormat.format("{0}\t{1}", env.getClock()
					.getCounter(), ss.getPower()));
		}
		pw.close();
	}

	@Test
	public void testGetPower2() throws Exception {
		Environment env = new Environment();
		SolarServer ss = new SolarServer(null, new Point2D.Double(-22.91792,
				127.61719));
		ss.basepower = 20;
		env.add(ss);
		env.run(8500000l, 8510000l);
	}
}
