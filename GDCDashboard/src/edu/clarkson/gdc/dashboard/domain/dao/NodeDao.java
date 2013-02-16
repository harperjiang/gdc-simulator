package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.List;

import edu.clarkson.gdc.dashboard.domain.entity.Node;

public interface NodeDao {

	public Node getNode(String id);

	public <T extends Node> List<T> getNodesByType(Class<T> clazz);
}
