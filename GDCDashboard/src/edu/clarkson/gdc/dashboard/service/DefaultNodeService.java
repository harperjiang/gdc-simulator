package edu.clarkson.gdc.dashboard.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;

import edu.clarkson.gdc.dashboard.domain.dao.HistoryDao;
import edu.clarkson.gdc.dashboard.domain.dao.NodeDao;
import edu.clarkson.gdc.dashboard.domain.dao.StatusDao;
import edu.clarkson.gdc.dashboard.domain.dao.VMDao;
import edu.clarkson.gdc.dashboard.domain.entity.Attributes;
import edu.clarkson.gdc.dashboard.domain.entity.Battery;
import edu.clarkson.gdc.dashboard.domain.entity.DataCenter;
import edu.clarkson.gdc.dashboard.domain.entity.HistoryType;
import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeHistory;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;
import edu.clarkson.gdc.dashboard.domain.entity.PowerSource;
import edu.clarkson.gdc.dashboard.domain.entity.StatusType;
import edu.clarkson.gdc.dashboard.domain.entity.VMStatus;
import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;

public class DefaultNodeService implements NodeService {

	private NodeDao nodeDao;

	private HistoryDao historyDao;

	private StatusDao statusDao;

	private VMDao vmDao;

	private long latency = 60000;

	@Override
	public String getData(String id) {
		Node node = nodeDao.getNode(id);
		Map<String, Object> data = new HashMap<String, Object>();
		if (null != node) {
			data.put("id", node.getId());
			data.put("name", node.getName());
			data.put("desc", node.getDescription());
			// Attributes
			data.putAll(node.getAttributes());
			if (node instanceof DataCenter) {
				data.put("type", "dc");
			}
			if (node instanceof Battery) {
				data.put("type", "battery");
			}
			if (node instanceof Machine) {
				data.put("type", "machine");
			}
			if (node instanceof PowerSource) {
				data.put("type", "power");
			}
			// Status
			Map<String, NodeStatus> status = statusDao.getStatus(node);
			if (null != status && !status.isEmpty())
				for (Entry<String, NodeStatus> se : status.entrySet()) {
					data.put(
							se.getKey(),
							se.getValue().getType()
									.convert(se.getValue().getValue()));
				}
			// History
			Map<String, Object> history = new HashMap<String, Object>();
			for (HistoryType type : HistoryType.values()) {
				List<NodeHistory> histories = historyDao.getHistories(node,
						type.name(), 50);
				if (!CollectionUtils.isEmpty(histories)) {
					// Reverse it
					Collections.reverse(histories);
					history.put(type.name(), histories);
				}
			}
			data.put("history", history);
		}
		return new Gson().toJson(data);
	}

	@Override
	public void updateStatus() {
		Map<String, DataCenter> dcs = new HashMap<String, DataCenter>();
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
			DataCenter dc = getNodeDao().up(node, DataCenter.class);
			if (Boolean.valueOf(status.getValue())
					&& !dcs.containsKey(dc.getId())) {
				dcs.put(dc.getId(), dc);
			}
		}
		// Update Data Center Status
		Set<DataCenter> alldc = new HashSet<DataCenter>();
		alldc.addAll(getNodeDao().getNodesByType(DataCenter.class));
		alldc.removeAll(dcs.values());
		for (DataCenter dc : dcs.values()) {
			NodeStatus status = new NodeStatus();
			status.setNodeId(dc.getId());
			status.setDataType(StatusType.STATUS.name());
			status.setValue("true");
			getStatusDao().updateStatus(status);
		}
		for (DataCenter dc : alldc) {
			NodeStatus status = new NodeStatus();
			status.setNodeId(dc.getId());
			status.setDataType(StatusType.STATUS.name());
			status.setValue("false");
			getStatusDao().updateStatus(status);
		}
	}

	@Override
	public void updatePowerUsage() {
		int green = 0;
		int grid = 0;
		// Calculate VM count in each DC
		for (DataCenter dc : getNodeDao().getNodesByType(DataCenter.class)) {
			List<Machine> machines = getNodeDao().down(dc, Machine.class);
			int count = 0;
			for (Machine machine : machines) {
				List<VirtualMachine> vms = getVmDao().list(machine);
				for (VirtualMachine vm : vms) {
					String vmStatus = vm.getAttributes().get(
							Attributes.VM_STATUS.attrName());
					if (VMStatus.RUNNING.value().equals(vmStatus)) {
						count++;
					}
				}
			}
			// Set DC Status & History
			NodeStatus status = new NodeStatus();
			status.setNodeId(dc.getId());
			status.setDataType(StatusType.DC_CONSUMPTION.name());
			status.setValue(String.valueOf(count));
			getStatusDao().updateStatus(status);

			NodeHistory history = new NodeHistory();
			history.setNodeId(dc.getId());
			history.setDataType(HistoryType.DC_CONSUMPTION.name());
			history.setValue(String.valueOf(count));
			history.setTime(new Date());
			getHistoryDao().addHistory(history);

			if (dc.getPowerSource() != null)
				green += count;
			else
				grid += count;
		}
		NodeStatus status = new NodeStatus();
		status.setNodeId(NodeDao.summaryNodeId);
		status.setDataType(StatusType.SUMMARY_VMRUNNING.name());
		status.setValue(String.valueOf(green + grid));
		getStatusDao().updateStatus(status);

		status = new NodeStatus();
		status.setNodeId(NodeDao.summaryNodeId);
		status.setDataType(StatusType.SUMMARY_UTIL.name());
		BigDecimal util = (green + grid == 0) ? BigDecimal.ZERO
				: new BigDecimal(green).divide(new BigDecimal(green + grid), 2,
						BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
		status.setValue(util.toPlainString());
		getStatusDao().updateStatus(status);
	}

	public NodeDao getNodeDao() {
		return nodeDao;
	}

	public void setNodeDao(NodeDao nodeDao) {
		this.nodeDao = nodeDao;
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

	public VMDao getVmDao() {
		return vmDao;
	}

	public void setVmDao(VMDao vmDao) {
		this.vmDao = vmDao;
	}

	public long getLatency() {
		return latency;
	}

	public void setLatency(long latency) {
		this.latency = latency;
	}
}
