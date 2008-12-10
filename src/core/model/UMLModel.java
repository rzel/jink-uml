package core.model;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import util.ByteBuffer;
import util.ByteReader;
import util.JinkUtils;
import core.model.node.SceneNode;

public class UMLModel {

	private final HashMap<SceneNode, HashSet<SceneNode>> graph = new HashMap<SceneNode, HashSet<SceneNode>>();
	private final HashMap<Integer, SceneNode> idNodes = new HashMap<Integer, SceneNode>();

	private static int ID = 0;

	private final int id;

	public UMLModel() {
		id = ID++;
	}

	public UMLModel(int id) {
		if (id >= ID)
			ID = id + 1;
		this.id = id;
	}

	public SceneNode nodeForID(int id) {
		return idNodes.get(id);
	}

	public void addNode(SceneNode n) {
		if (graph.containsKey(n))
			return;
		graph.put(n, new HashSet<SceneNode>());
		idNodes.put(n.getID(), n);
	}

	public Iterator<SceneNode> getNodeIterator() {
		return graph.keySet().iterator();
	}

	public SceneNode getNodeAt(int x, int y) {
		SceneNode ret = null;
		for (SceneNode sn : graph.keySet()) {
			if (sn.getBounds().contains(x, y)) {
				ret = sn;
			}
		}
		return ret;
	}

	public int getID() {
		return id;
	}

	public void addLink(SceneNode from, SceneNode to) {
		if (from != to)
			graph.get(from).add(to);
	}

	public HashMap<SceneNode, HashSet<SceneNode>> getGraph() {
		return graph;
	}

	public void removeNode(SceneNode selected) {
		idNodes.remove(selected.getID());
		graph.remove(selected);
		for (SceneNode sn : graph.keySet()) {
			graph.get(sn).remove(selected);
		}
	}

	public void writeTo(ByteBuffer b) {
		b.addShort((short) id);
		b.addShort((short) graph.size());
		for (SceneNode sn : graph.keySet()) {
			sn.writeTo(b);
		}
		for (SceneNode sn : graph.keySet()) {
			HashSet<SceneNode> links = graph.get(sn);
			b.addShort((short) sn.getID());
			b.add(links.size());
			for (SceneNode link : links) {
				b.addShort((short) link.getID());
			}
		}
	}

	public static UMLModel readFrom(ByteReader b) {
		int id = b.readShort();
		UMLModel model = new UMLModel(id);
		int graphSize = b.readShort();
		for (int i = 0; i < graphSize; i++) {
			SceneNode sn = SceneNode.readFrom(b);
			model.addNode(sn);
		}
		for (int i = 0; i < graphSize; i++) {
			SceneNode from = model.nodeForID(b.readShort());
			int numLinks = b.read();
			for (int j = 0; j < numLinks; j++) {
				SceneNode to = model.nodeForID(b.readShort());
				model.addLink(from, to);
			}
		}
		return model;
	}

	public void removeLinksFrom(SceneNode selected) {
		graph.get(selected).clear();
	}

	public Rectangle getBounds() {
		return JinkUtils.calculateBounds(graph.keySet().iterator());
	}

}
