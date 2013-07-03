package edu.clarkson.gdc.dashboard.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;

import edu.clarkson.gdc.dashboard.domain.dao.HistoryDao;
import edu.clarkson.gdc.dashboard.domain.dao.NodeDao;
import edu.clarkson.gdc.dashboard.domain.dao.StatusDao;
import edu.clarkson.gdc.dashboard.domain.entity.Battery;
import edu.clarkson.gdc.dashboard.domain.entity.DataCenter;
import edu.clarkson.gdc.dashboard.domain.entity.HistoryType;
import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeHistory;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;
import edu.clarkson.gdc.dashboard.domain.entity.PowerSource;
import edu.clarkson.gdc.dashboard.domain.entity.StatusType;

public class DefaultNodeService implements NodeService {

	private NodeDao nodeDao;

	private HistoryDao historyDao;

	private StatusDao statusDao;

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

	public long getLatency() {
		return latency;
	}

	public void setLatency(long latency) {
		this.latency = latency;
	}

}
