package core.model;

import java.util.LinkedList;

import core.model.node.SceneNode;

public abstract class SceneNodeSet {

	public abstract LinkedList<String> getNodeNames();

	public abstract SceneNode createNode(String type);

}
