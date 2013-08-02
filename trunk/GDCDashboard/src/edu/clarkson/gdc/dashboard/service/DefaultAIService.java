package edu.clarkson.gdc.dashboard.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.dashboard.domain.dao.HistoryDao;
import edu.clarkson.gdc.dashboard.domain.dao.NodeDao;
import edu.clarkson.gdc.dashboard.domain.dao.StatusDao;
import edu.clarkson.gdc.dashboard.domain.dao.VMDao;
import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.dashboard.domain.entity.Attributes;
import edu.clarkson.gdc.dashboard.domain.entity.DataCenter;
import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;
import edu.clarkson.gdc.dashboard.domain.entity.StatusType;
import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;
import edu.clarkson.gdc.dashboard.service.ai.NodeScore;

public class DefaultAIService implements AIService {

	private NodeDao nodeDao;

	private HistoryDao historyDao;

	private StatusDao statusDao;

	private VMDao vmDao;

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
		Node node = null;
		switch (alert.getType()) {
		case POWER_TOO_LOW:
			// Migrate all vms under the Data Center out
			node = getNodeDao().up(getNodeDao().getNode(alert.getNodeId()),
					DataCenter.class);
			migrateOut(node, true);
			break;
		case POWER_IS_HIGH:
			node = getNodeDao().up(getNodeDao().getNode(alert.getNodeId()),
					DataCenter.class);
			migrateIn(node);
			break;
		default:
			break;
		}
		return;
	}

	protected void migrateOut(Node node, boolean force) {
		if (null == node)
			return;
		Map<String, String[]> migration = makeMigrateDecision(
				nodeDao.up(node, DataCenter.class), force);
		for (Entry<String, String[]> entry : migration.entrySet()) {
			Machine source = getNodeDao().getNode(entry.getValue()[0]);
			Machine dest = getNodeDao().getNode(entry.getValue()[1]);
			VirtualMachine vm = getVmDao().find(source, entry.getKey());
			getVmDao().migrate(vm, source, dest);
		}
	}

	protected void migrateIn(Node node) {
		if (null == node)
			return;
		List<DataCenter> alldss = nodeDao.getNodesByType(DataCenter.class);
		alldss.remove(nodeDao.up(node, DataCenter.class));
		for (DataCenter ds : alldss) {
			Map<String, String[]> migration = makeMigrateDecision(ds, false);
			for (Entry<String, String[]> entry : migration.entrySet()) {
				Machine source = getNodeDao().getNode(entry.getValue()[0]);
				Machine dest = getNodeDao().getNode(entry.getValue()[1]);
				VirtualMachine vm = getVmDao().find(source, entry.getKey());
				getVmDao().migrate(vm, source, dest);
			}
		}
	}

	protected Map<String, String[]> makeMigrateDecision(DataCenter node,
			boolean force) {
		Validate.notNull(node);
		List<Machine> machines = getNodeDao().down(node, Machine.class);
		// FIXME Remove non-automig machines
		Iterator<Machine> smi = machines.iterator();
		while (smi.hasNext()) {
			Machine m = smi.next();
			if (m.getAttributes().containsKey("automig")) {
				String automig = m.getAttributes().get("automig");
				if ("false".equals(automig)) {
					smi.remove();
				}
			}
		}

		NodeScore baseScore = score(node);

		// Find potential target data centers
		List<Machine> available = new ArrayList<Machine>();
		List<DataCenter> dss = getNodeDao().getNodesByType(DataCenter.class);
		if (force) {
			dss.remove(node);
		}
		for (DataCenter ds : dss) {
			NodeScore dsScore = score(ds);
			if (force || dsScore.getScore() > baseScore.getScore()) {
				available.addAll(getNodeDao().down(ds, Machine.class));
			}
		}
		// Remove unavailable machine
		Iterator<Machine> mi = available.iterator();
		while (mi.hasNext()) {
			Machine machine = mi.next();
			// FIXME This should be removed after the virtual network issue is
			// solved
			if (machine.getAttributes().containsKey("automig")) {
				String automig = machine.getAttributes().get("automig");
				if ("false".equals(automig)) {
					mi.remove();
					continue;
				}
			}

			NodeStatus sta = getStatusDao().getStatus(machine,
					StatusType.STATUS);
			if ("false".equals(sta.getValue()))
				mi.remove();
		}
		// Randomly choose between them
		Map<String, String[]> result = new HashMap<String, String[]>();
		if (available.size() == 0)
			return result;
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
				}
			}
		}
		return result;
	}

	protected NodeScore score(Machine machine, Node against) {
		NodeScore score = new NodeScore();
		score.setNode(machine);

		int vmCount = Integer.valueOf(getStatusDao().getStatus(machine,
				StatusType.MACHINE_VMCOUNT).getValue());
		int total = 100; // default value
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
