package core.model.node;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import util.ByteBuffer;
import util.ByteReader;

/**
 * Contains a whole new scene inside of this scene.
 * 
 * @author Jason
 * 
 */
public class RecursiveSceneNode extends SceneNode {

	public static final int ID = 0;

	private BufferedImage glimpse = null;

	public RecursiveSceneNode() {
	}

	public RecursiveSceneNode(int id, String name, Rectangle bounds) {
		super(id, name, bounds);
	}

	@Override
	public Dimension getInitalSize() {
		return new Dimension(100, 100);
	}

	@Override
	public String getNodeType() {
		return "Node";
	}

	@Override
	protected int getOutputID() {
		return ID;
	}

	@Override
	protected void readAttributes(ByteReader bb) {
	}

	@Override
	protected void writeAttributes(ByteBuffer bb) {
	}

	public void setGlimpse(BufferedImage bi) {
		this.glimpse = bi;
	}

	public BufferedImage getGlimpse() {
		return glimpse;
	}

}
