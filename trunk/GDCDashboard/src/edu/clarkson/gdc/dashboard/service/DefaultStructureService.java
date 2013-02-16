package edu.clarkson.gdc.dashboard.service;

import java.util.List;

import edu.clarkson.gdc.dashboard.domain.dao.NodeDao;
import edu.clarkson.gdc.dashboard.domain.entity.DataCenter;

public class DefaultStructureService implements StructureService {

	private NodeDao nodeDao;

	@Override
	public List<DataCenter> getDataCenters() {
		return nodeDao.getNodesByType(DataCenter.class);
	}

	public NodeDao getNodeDao() {
		return nodeDao;
	}

	public void setNodeDao(NodeDao nodeDao) {
		this.nodeDao = nodeDao;
	}

}
