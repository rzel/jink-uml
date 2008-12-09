package core.model.node.planning;

import java.awt.Dimension;
import java.awt.Rectangle;

import util.ByteBuffer;
import util.ByteReader;
import core.model.node.SceneNode;

public class LinkNode extends SceneNode {

	private static final int ID = 3;

	private String url;

	public LinkNode() {
		url = "http://";
	}

	public LinkNode(int id, String name, Rectangle bounds) {
		super(id, name, bounds);
	}

	@Override
	public Dimension getInitalSize() {
		return new Dimension(180, 50);
	}

	@Override
	public String getNodeType() {
		return "Link";
	}

	@Override
	protected int getOutputID() {
		return ID;
	}

	@Override
	protected void readAttributes(ByteReader bb) {
		url = bb.readString();
	}

	@Override
	protected void writeAttributes(ByteBuffer bb) {
		bb.addString(url);
	}

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

}
