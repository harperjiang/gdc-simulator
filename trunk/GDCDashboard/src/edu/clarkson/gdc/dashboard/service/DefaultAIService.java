package edu.clarkson.gdc.dashboard.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import edu.clarkson.gdc.dashboard.domain.dao.HistoryDao;
import edu.clarkson.gdc.dashboard.domain.dao.NodeDao;
import edu.clarkson.gdc.dashboard.domain.dao.StatusDao;
import edu.clarkson.gdc.dashboard.domain.dao.VMDao;
import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.dashboard.domain.entity.Attributes;
import edu.clarkson.gdc.dashboard.domain.entity.Battery;
import edu.clarkson.gdc.dashboard.domain.entity.DataCenter;
import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeHistory;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;
import edu.clarkson.gdc.dashboard.domain.entity.PowerSource;
import edu.clarkson.gdc.dashboard.domain.entity.StatusType;
import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;
import edu.clarkson.gdc.dashboard.service.ai.NodeScore;

public class DefaultAIService implements AIService {

	private NodeDao nodeDao;

	private HistoryDao historyDao;

	private StatusDao statusDao;

	private VMDao vmDao;

	private long latency = 30000;

	@Override
	public void updateStatus() {
		long current = System.currentTimeMillis();
		List<Node> nodes = new ArrayList<Node>();
		nodes.addAll(nodeDao.getNodesByType(Machine.class));
		nodes.addAll(nodeDao.getNodesByType(Battery.class));
		nodes.addAll(nodeDao.getNodesByType(PowerSource.class));
		// Update Running Status
		for (Node node : nodes) {
			NodeHistory latest = historyDao.getLatest(node, null);
			NodeStatus status = new NodeStatus();
			status.setDataType(StatusType.STATUS.name());
			if (latest == null)
				status.setValue("false");
			else
				status.setValue(Boolean.toString(current
						- latest.getTime().getTime() <= latency));
			status.setNodeId(node.getId());
			statusDao.updateStatus(status);
		}
	}

	@Override
	public void relocateVM() {
		List<DataCenter> dsList = getNodeDao().getNodesByType(DataCenter.class);
		for (DataCenter ds : dsList)
			migrateOut(ds, false);
	}

	@Override
	public void handleAlert(Alert alert) {
		if (null == alert) {
			return;
		}
		switch (alert.getType()) {
		case BTY_LOW_LEVEL:
			// Migrate all vms under the Data Center out
			Node node = getNodeDao().up(
					getNodeDao().getNode(alert.getNodeId()), DataCenter.class);
			migrateOut(node, true);
			break;
		default:
			break;
		}
		return;
	}

	protected void migrateOut(Node node, boolean force) {
		Map<String, String[]> migration = makeMigrateDecision(nodeDao.up(node,
				DataCenter.class));
		for (Entry<String, String[]> entry : migration.entrySet()) {
			VirtualMachine vm = getNodeDao().getNode(entry.getKey());
			Machine source = getNodeDao().getNode(entry.getValue()[0]);
			Machine dest = getNodeDao().getNode(entry.getValue()[1]);
			getVmDao().migrate(vm, source, dest);
		}
	}

	protected Map<String, String[]> makeMigrateDecision(DataCenter node) {
		List<Machine> machines = getNodeDao().down(node, Machine.class);
		NodeScore baseScore = score(node);

		// Find potential target data centers
		List<Machine> available = new ArrayList<Machine>();
		for (DataCenter ds : getNodeDao().getNodesByType(DataCenter.class)) {
			NodeScore dsScore = score(ds);
			if (dsScore.getScore() > baseScore.getScore()) {
				available.addAll(getNodeDao().down(ds, Machine.class));
			}
		}
		// Randomly choose between them
		List<NodeScore> scores = new ArrayList<NodeScore>();
		Random random = new Random(System.currentTimeMillis());
		int total = 0;
		for (Machine avail : available) {
			NodeScore score = score(avail, node);
			if (score.getAvail() == 0) {
				continue;
			}
			total += score.getScore();
			scores.add(score);
		}
		Map<String, String[]> result = new HashMap<String, String[]>();
		for (Machine out : machines) {
			List<VirtualMachine> vms = getVmDao().list(out);
			for (VirtualMachine vm : vms) {
				// Determine where to migrate
				int pos = random.nextInt(total);
				for (int i = 0; i < scores.size(); i++) {
					NodeScore score = scores.get(i);
					if (pos < score.getScore()) {
						result.put(vm.getName(), new String[] { out.getId(),
								score.getNode().getId() });
						score.setAvail(score.getAvail() - 1);
						if (score.getAvail() == 0) {
							scores.remove(score);
							total -= score.getScore();
						}
						break;
					}
					pos -= score.getScore();
					i++;
				}
			}
		}
		return result;
	}

	protected NodeScore score(Machine machine, Node against) {
		NodeScore score = new NodeScore();
		score.setNode(machine);

		int vmCount = Integer.valueOf(getStatusDao().getStatus(machine,
				StatusType.MACHINE_VMCOUNT.name()).getValue());
		int total = -1;
		String totalStr = machine.getAttributes().get(
				Attributes.MACHINE_VMSIZE.attrName());
		if (StringUtils.isNotEmpty(totalStr)) {
			total = Integer.valueOf(totalStr);
		}
		score.setAvail(total - vmCount);
		// TODO Update Score Algorithm
		score.setScore(10);
		return score;
	}

	protected NodeScore score(DataCenter ds) {
		NodeScore score = new NodeScore();
		score.setNode(ds);
		score.setScore(ds.getPowerSource() == null ? 0 : 100);
		return score;
	}

	public HistoryDao getHistoryDao() {
		return historyDao;
	}

	public void setHistoryDao(HistoryDao historyDao) {
		this.historyDao = historyDao;
	}

	public StatusDao getStatusDao() {
		return statusDao;
	}

	public void setStatusDao(StatusDao statusDao) {
		this.statusDao = statusDao;
	}

	public long getLatency() {
		return latency;
	}

	public void setLatency(long latency) {
		this.latency = latency;
	}

	public NodeDao getNodeDao() {
		return nodeDao;
	}

	public void setNodeDao(NodeDao nodeDao) {
		this.nodeDao = nodeDao;
	}

	public VMDao getVmDao() {
		return vmDao;
	}

	public void setVmDao(VMDao vmDao) {
		this.vmDao = vmDao;
	}
}
