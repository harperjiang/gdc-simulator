package edu.clarkson.gdc.simulator.scenario.latency.solar;

import java.awt.geom.Point2D;

import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.ProcessTimeModel.ConstantTimeModel;

public class DistancePipe extends Pipe {

	public DistancePipe(Node source, Node destination) {
		super(source, destination);
		// Calculate Latency
		long latency = calculate(source, destination);
		setTimeModel(new ConstantTimeModel(latency));
	}

	protected static long calculate(Node source, Node dest) {
		Point2D.Double point1 = source.getLocation();
		Point2D.Double point2 = dest.getLocation();

		// Calculate distance
		double rada = point1.x * Math.PI / 180;
		double radb = point2.x * Math.PI / 180;

		double cosa = Math.cos(rada);
		double cosb = Math.cos(radb);
		double c = Math.pow(cosa, 2) + Math.pow(cosb, 2) - 2 * cosa * cosb
				* Math.cos((point1.y - point2.y) * Math.PI / 180);
		double a = Math.sin(point1.x * 2 * Math.PI / 360);
		double b = Math.sin(point2.x * 2 * Math.PI / 360);

		double linedist = Math.sqrt((a - b) * (a - b) + c);

		double angle = Math.acos(1 - linedist * linedist / 2);
		// Earth radius is around 6000KM
		if (angle < 0.15) { // < 1000KM, local
			return 5;
		}
		if (angle < 0.67) { // < 4000KM, within country
			return 20;
		}
		return (long) (100 + angle * 50);
	}
}
