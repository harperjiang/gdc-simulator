package gdc.network.model;

import java.math.BigDecimal;

public class Edge {

	private Node source;

	private Node destination;

	private BigDecimal value;

	public Edge(Node source, Node destination, BigDecimal value) {
		this.source = source;
		this.destination = destination;
		this.value = value;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getDestination() {
		return destination;
	}

	public void setDestination(Node destination) {
		this.destination = destination;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}
