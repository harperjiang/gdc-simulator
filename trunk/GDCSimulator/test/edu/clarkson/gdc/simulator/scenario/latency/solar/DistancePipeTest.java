package edu.clarkson.gdc.simulator.scenario.latency.solar;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.Test;

import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.scenario.latency.solar.DistancePipe;

public class DistancePipeTest {

	@Test
	public void testCalculate() {
		Node a = new Node() {
			public <T> T getLocation() {
				return (T)new Point2D.Double(0d,0d);
			}
		};
		Node b = new Node() {
			public <T> T getLocation() {
				return (T)new Point2D.Double(0d,180d);
			}
		};
		long val = DistancePipe.calculate(a, b);
		assertEquals(257,val);
	}
}
