package core.model;

import java.util.LinkedList;

import core.model.node.RecursiveSceneNode;
import core.model.node.SceneNode;
import core.model.node.planning.LinkNode;

public class PlanningSceneNodeSet extends SceneNodeSet {

	private static final String[] NAMES = new String[] { "Node", "Link" };
	private final LinkedList<String> nodeNames;

	public PlanningSceneNodeSet() {
		nodeNames = new LinkedList<String>();
		for (String s : NAMES) {
			nodeNames.add(s);
		}
	}

	@Override
	public LinkedList<String> getNodeNames() {
		return nodeNames;
	}

	@Override
	public SceneNode createNode(String type) {
		if (type.equals("Node")) {
			return new RecursiveSceneNode();
		} else if (type.equals("Link")) {
			return new LinkNode();
		} else {
			throw new RuntimeException("Couldn't find node of type: " + type);
		}
	}

}
