package edu.clarkson.gdc.dashboard.service;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import edu.clarkson.gdc.dashboard.domain.dao.NodeDao;
import edu.clarkson.gdc.dashboard.domain.entity.Battery;
import edu.clarkson.gdc.dashboard.domain.entity.DataCenter;
import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.Node;

public class DefaultNodeService implements NodeService {

	private NodeDao nodeDao;

	@Override
	public String getData(String id) {
		Node node = nodeDao.getNode(id);
		Map<String, Object> data = new HashMap<String, Object>();
		if (null != node) {
			data.put("id", node.getId());
			data.put("name", node.getName());
			data.put("description", node.getDescription());
			if (node instanceof DataCenter) {
				data.put("type", "dc");
			}
			if (node instanceof Battery) {
				data.put("type", "battery");
			}
			if (node instanceof Machine) {
				data.put("type", "machine");
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

}
