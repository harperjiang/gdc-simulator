package edu.clarkson.gdc.dashboard.service.ai;

import edu.clarkson.gdc.dashboard.domain.entity.Node;

public class NodeScore {

	private Node node;

	private int score;

	private int avail;

	@SuppressWarnings("unchecked")
	public <T extends Node> T getNode() {
		return (T) node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getAvail() {
		return avail;
	}

	public void setAvail(int avail) {
		this.avail = avail;
	}

}
