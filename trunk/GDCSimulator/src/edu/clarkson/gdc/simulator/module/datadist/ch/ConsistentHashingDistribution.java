package edu.clarkson.gdc.simulator.module.datadist.ch;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.simulator.Cloud;
import edu.clarkson.gdc.simulator.DataBlockDistribution;
import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.framework.NodeState;
import edu.clarkson.gdc.simulator.framework.NodeStateEvent;
import edu.clarkson.gdc.simulator.framework.NodeStateListener;
import edu.clarkson.gdc.simulator.scenario.latency.simple.DefaultCloud;

public class ConsistentHashingDistribution implements DataBlockDistribution,
		NodeStateListener {

	private ConsistentHashing hashing;

	// How many servers a read operation will read from
	private int readcopy = 1;

	// How many servers a write operation will write to
	private int writecopy = 1;

	public ConsistentHashingDistribution() {
		super();

		hashing = new ConsistentHashing();
	}

	@Override
	public void init(Cloud cloud) {
		Validate.isTrue(cloud instanceof DefaultCloud);
		DefaultCloud defaultc = (DefaultCloud) cloud;
		// Init Key Allocation Between Data Centers

		for (DataCenter dc : cloud.getDataCenters()) {
			hashing.add(dc.getId());
		}

		defaultc.addListener(NodeStateListener.class, this);
	}

	@Override
	public List<String> locate(String key) {
		List<String> result = new ArrayList<String>();
		result.addAll(hashing.get(key, readcopy));
		return result;
	}

	public List<String> choose(String key) {
		List<String> result = new ArrayList<String>();
		result.addAll(hashing.get(key, writecopy));
		return result;
	}

	@Override
	public void stateChanged(NodeStateEvent event) {
		if (event.getSource() instanceof DataCenter) {
			DataCenter dc = (DataCenter) event.getSource();
			// Keep informed on datacenter startup/shutdown/exception
			if (NodeState.DOWN == event.getFrom()
					|| NodeState.EXCEPTION == event.getFrom()) {
				hashing.add(dc.getId());
			}
			if (NodeState.DOWN == event.getTo()
					|| NodeState.EXCEPTION == event.getTo()) {
				hashing.remove(dc.getId());
			}
		}
	}

	public int getReadcopy() {
		return readcopy;
	}

	public void setReadcopy(int readcopy) {
		this.readcopy = readcopy;
	}

	public int getWritecopy() {
		return writecopy;
	}

	public void setWritecopy(int writecopy) {
		this.writecopy = writecopy;
	}
}
