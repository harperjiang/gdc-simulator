package edu.clarkson.gdc.dashboard.domain.entity;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

	private TreeNode parent;

	private List<TreeNode> children;

	private String id;

	private String name;

	public TreeNode() {
		super();
		children = new ArrayList<TreeNode>();
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
		parent.getChildren().add(this);
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
