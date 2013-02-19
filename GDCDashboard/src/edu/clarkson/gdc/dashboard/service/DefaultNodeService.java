package edu.clarkson.gdc.dashboard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

public class DefaultNodeService implements NodeService {

	private NodeDao nodeDao;

	private HistoryDao historyDao;

	private StatusDao statusDao;

	@Override
	public String getData(String id) {
		Node node = nodeDao.getNode(id);
		Map<String, Object> data = new HashMap<String, Object>();
		if (null != node) {
			data.put("id", node.getId());
			data.put("name", node.getName());
			data.put("desc", node.getDescription());
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
			for (HistoryType type : HistoryType.values()) {
				List<NodeHistory> histories = historyDao.getHistories(node,
						type.name(), 50);
				data.put(type.name(), histories);
			}
		}
		return new Gson().toJson(data);
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

}
