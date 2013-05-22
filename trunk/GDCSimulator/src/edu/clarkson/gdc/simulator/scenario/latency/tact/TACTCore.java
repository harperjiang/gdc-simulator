package edu.clarkson.gdc.simulator.scenario.latency.tact;

import java.util.ArrayList;
import java.util.List;

public class TACTCore {

	private int owner;

	private int size;

	// Tentative operations
	private SortingList<Operation> tentative;

	private List<Operation> buffer;

	private long[] timeVector;

	private ArrayList<ArrayList<Operation>> queues;

	private int pullCount = 0;

	public TACTCore(int owner, int size) {
		this.owner = owner;
		this.size = size;
		this.tentative = new SortingList<Operation>();

		this.buffer = new ArrayList<Operation>();

		queues = new ArrayList<ArrayList<Operation>>();
		this.timeVector = new long[size];
		for (int i = 0; i < timeVector.length; i++) {
			queues.add(new ArrayList<Operation>());
			timeVector[i] = 0;
		}
	}

	public void add(Operation operation) {
		buffer.add(operation);
	}

	public void receive(int index, Long endPoint, List<Operation> list) {
		for (Operation opr : list)
			tentative.add(opr);
		timeVector[index] = endPoint;
	}

	public void receivepull(int index, Long endPoint, List<Operation> list) {
		receive(index, endPoint, list);
		pullCount--;
	}

	public List<Operation> push(int index) {
		ArrayList<Operation> topush = new ArrayList<Operation>();
		topush.addAll(queues.get(index));
		queues.get(index).clear();
		return topush;
	}

	public List<Operation> bepulled(int serverNum) {
		return push(serverNum);
	}

	public void pull() {
		pullCount = size - 1;
	}

	public boolean isPulling() {
		return pullCount != 0;
	}

	public long getTime(int index) {
		return timeVector[index];
	}

	public boolean commit() {
		if (tentative.size() == 0)
			return false;
		long min = mintime();
		Operation location = new Operation(min, size, null);
		List<Operation> tocommit = tentative.removebefore(location);
		return tocommit.size() > 0;
	}

	protected long mintime() {
		long min = Integer.MAX_VALUE;
		for (int i = 0; i < size; i++) {
			min = Math.min(timeVector[i], min);
		}
		return min;
	}

	public boolean hasOrderError() {
		return ordererr >= 0 && tentative.size() > ordererr;
	}

	public boolean hasNumError(int index) {
		return numerr >= 0 && queues.get(index).size() >= numerr;
	}

	public boolean isStale(int index, long counter) {
		return staleness >= 0 && counter - timeVector[index] >= staleness
				&& queues.get(index).size() > 0;
	}

	// One Round Done
	public void summarize() {
		for (Operation opr : buffer) {
			for (int i = 0; i < size; i++) {
				if (i != owner) {
					queues.get(i).add(opr);
				}
			}
			tentative.add(opr);
		}
		buffer.clear();
	}

	private int numerr;

	private int ordererr;

	private long staleness;

	public int getNumerr() {
		return numerr;
	}

	public void setNumerr(int numerr) {
		this.numerr = numerr;
	}

	public int getOrdererr() {
		return ordererr;
	}

	public void setOrdererr(int ordererr) {
		this.ordererr = ordererr;
	}

	public long getStaleness() {
		return staleness;
	}

	public void setStaleness(long staleness) {
		this.staleness = staleness;
	}
}