package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.List;

import edu.clarkson.gdc.dashboard.domain.entity.Node;

public interface NodeDao {

	String summaryNodeId = "summary";

	public <T extends Node> T getNode(String id);

	public <T extends Node> List<T> getNodesByType(Class<T> clazz);

	public <T extends Node> List<T> down(Node parent, Class<T> filter);

	public <T extends Node> T up(Node child, Class<T> filter);
}
