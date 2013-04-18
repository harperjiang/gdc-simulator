package edu.clarkson.gdc.simulator.impl.solar;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.Test;

import edu.clarkson.gdc.simulator.framework.Node;

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
		assertEquals(100,val);
	}
}
