package edu.clarkson.gdc.dashboard.service.ai;

import edu.clarkson.gdc.dashboard.domain.entity.Machine;

public class MachineScore {

	private Machine machine;

	private int score;

	private int avail;

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
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
