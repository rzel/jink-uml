package core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import core.model.node.SceneNode;

public class UMLModel {

	private final LinkedHashSet<SceneNode> nodes;
	private final HashMap<SceneNode, HashSet<SceneNode>> graph = new HashMap<SceneNode, HashSet<SceneNode>>();

	public UMLModel() {
		nodes = new LinkedHashSet<SceneNode>();
	}

	public void addNode(SceneNode n) {
		if (nodes.add(n))
			graph.put(n, new HashSet<SceneNode>());
	}

	public Iterator<SceneNode> getNodeIterator() {
		return nodes.iterator();
	}

	public SceneNode getNodeAt(int x, int y) {
		SceneNode ret = null;
		for (SceneNode sn : nodes) {
			if (sn.getBounds().contains(x, y)) {
				ret = sn;
			}
		}
		return ret;
	}

	public void addLink(SceneNode selected, SceneNode arrowTo) {
		graph.get(selected).add(arrowTo);
	}

	public HashMap<SceneNode, HashSet<SceneNode>> getGraph() {
		return graph;
	}

	public void removeNode(SceneNode selected) {
		nodes.remove(selected);
		graph.remove(selected);
		for (SceneNode sn : graph.keySet()) {
			graph.get(sn).remove(selected);
		}
	}

}
