package core.model.node;

import java.awt.Dimension;
import java.awt.Rectangle;

import util.ByteBuffer;
import util.ByteReader;
import core.model.node.java.ClassNode;
import core.model.node.java.InterfaceNode;

/**
 * The super-class of all nodes.
 * 
 * @author Jason
 * 
 */
public abstract class SceneNode {

	private static int ID = 0;

	protected final int id;
	protected Rectangle bounds;
	protected String name;

	public SceneNode() {
		id = ID++;
		Dimension dim = getInitalSize();
		bounds = new Rectangle(0, 0, dim.width, dim.height);
		name = getNodeType();
	}

	public SceneNode(int id, String name, Rectangle bounds) {
		if (id >= ID)
			ID = id;
		this.id = id;
		this.name = name;
		this.bounds = bounds;
	}

	public void setName(String s) {
		name = s;
	}

	public abstract Dimension getInitalSize();

	public abstract String getNodeType();

	protected abstract int getOutputID();

	protected abstract void writeAttributes(ByteBuffer bb);

	protected abstract void readAttributes(ByteReader bb);

	public void setLocation(int x, int y) {
		bounds.setLocation(x, y);
	}

	public int getWidth() {
		return bounds.width;
	}

	public void setSize(int width, int height) {
		bounds.setSize(width, height);
	}

	public int getHeight() {
		return bounds.height;
	}

	public int getX() {
		return bounds.x;
	}

	public int getY() {
		return bounds.y;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

	public int getID() {
		return id;
	}

	public final void writeTo(ByteBuffer b) {
		b.add(getOutputID());
		b.addShort((short) id);
		b.addString(name);
		b.addInt(bounds.x);
		b.addInt(bounds.y);
		b.addInt(bounds.width);
		b.addInt(bounds.height);
		writeAttributes(b);
	}

	public static SceneNode readFrom(ByteReader b) {
		int type = b.read();
		int id = b.readShort();
		String name = b.readString();
		int x = b.readInt();
		int y = b.readInt();
		int w = b.readInt();
		int h = b.readInt();
		Rectangle bounds = new Rectangle(x, y, w, h);
		SceneNode node = null;
		if (type == RecursiveSceneNode.ID) {
			node = new RecursiveSceneNode(id, name, bounds);
		} else if (type == ClassNode.ID) {
			node = new ClassNode(id, name, bounds);
		} else if (type == InterfaceNode.ID) {
			node = new InterfaceNode(id, name, bounds);
		}
		if (node == null)
			throw new RuntimeException("Don't recognize node with ID=" + id);
		node.readAttributes(b);
		return node;
	}

	public static final SceneNode ROOT_NODE = new RecursiveSceneNode(0, "root",
			new Rectangle());

}
