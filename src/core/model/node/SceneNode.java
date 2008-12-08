package core.model.node;

import java.awt.Dimension;
import java.awt.Rectangle;

/**
 * The super-class of all nodes.
 * 
 * @author Jason
 * 
 */
public abstract class SceneNode {

	protected Rectangle bounds;
	protected String name;

	public SceneNode() {
		Dimension dim = getInitalSize();
		bounds = new Rectangle(0, 0, dim.width, dim.height);
		name = getNodeType();
	}

	public void setName(String s) {
		name = s;
	}

	public abstract Dimension getInitalSize();

	public abstract String getNodeType();

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
}
