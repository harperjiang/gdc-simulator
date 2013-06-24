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
import edu.clarkson.gdc.dashboard.domain.entity.DataCenter;
import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeHistory;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;
import edu.clarkson.gdc.dashboard.domain.entity.StatusType;
import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;
import edu.clarkson.gdc.dashboard.service.ai.MachineScore;

public class DefaultAIService implements AIService {

	private NodeDao nodeDao;

	private HistoryDao historyDao;

	private StatusDao statusDao;

	private VMDao vmDao;

	private long latency = 30000;

	@Override
	public void updateStatus() {
		long current = System.currentTimeMillis();
		// Update Running Status
		for (Machine machine : nodeDao.getNodesByType(Machine.class)) {
			NodeHistory latest = historyDao.getLatest(machine, null);
			NodeStatus status = new NodeStatus();
			status.setDataType(StatusType.STATUS.name());
			if (latest == null)
				status.setValue("false");
			else
				status.setValue(Boolean.toString(current
						- latest.getTime().getTime() <= latency));
			status.setNodeId(machine.getId());
			statusDao.updateStatus(status);
		}
	}

	@Override
	public void handleAlert(Alert alert) {
		if (null == alert) {
			return;
		}
		switch (alert.getType()) {
		case POWER_EXHAUST:
			// Migrate all vms under the Data Center
			Node node = getNodeDao().getNode(alert.getNodeId());
			migrateOut(node);
			break;
		default:
			break;
		}
		return;
	}

	protected void migrateOut(Node node) {
		Map<String, String[]> migration = makeMigrateDecision(nodeDao.up(node,
				DataCenter.class));
		for (Entry<String, String[]> entry : migration.entrySet()) {
			VirtualMachine vm = getNodeDao().getNode(entry.getKey());
			Machine source = getNodeDao().getNode(entry.getValue()[0]);
			Machine dest = getNodeDao().getNode(entry.getValue()[1]);
			getVmDao().migrate(vm, source, dest);
		}
	}

	protected Map<String, String[]> makeMigrateDecision(Node node) {
		List<Machine> machines = getNodeDao().down(node, Machine.class);
		List<Machine> available = getNodeDao().getNodesByType(Machine.class);
		available.removeAll(machines);
		List<MachineScore> scores = new ArrayList<MachineScore>();
		Random random = new Random(System.currentTimeMillis());
		int total = 0;
		for (Machine avail : available) {
			MachineScore score = score(avail, node);
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
					MachineScore score = scores.get(i);
					if (pos < score.getScore()) {
						result.put(vm.getName(), new String[] { out.getId(),
								score.getMachine().getId() });
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

	protected MachineScore score(Machine machine, Node against) {
		// TODO Update Score Algorithm
		MachineScore score = new MachineScore();
		score.setMachine(machine);

		int vmCount = Integer.valueOf(getStatusDao().getStatus(machine,
				StatusType.MACHINE_VMCOUNT.name()).getValue());
		int total = -1;
		String totalStr = machine.getAttributes().get(
				Attributes.MACHINE_VMSIZE.attrName());
		if (StringUtils.isNotEmpty(totalStr)) {
			total = Integer.valueOf(totalStr);
		}
		score.setAvail(total - vmCount);
		return score;
	}

	@Override
	public void relocateVM() {
		// TODO Auto-generated method stub

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
