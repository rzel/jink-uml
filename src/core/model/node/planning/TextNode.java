package core.model.node.planning;

import java.awt.Dimension;
import java.awt.Rectangle;

import util.ByteBuffer;
import util.ByteReader;
import core.model.node.SceneNode;

public class TextNode extends SceneNode {

	public static final int ID = 4;

	private String text;

	public TextNode() {
		text = "";
	}

	public TextNode(int id, String name, Rectangle bounds) {
		super(id, name, bounds);
	}

	@Override
	public Dimension getInitalSize() {
		return new Dimension(180, 80);
	}

	public void setText(String text) {
		if (text == null)
			text = "";
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public String getNodeType() {
		return "Text";
	}

	@Override
	protected int getOutputID() {
		return ID;
	}

	@Override
	protected void readAttributes(ByteReader bb) {
		text = bb.readString();
	}

	@Override
	protected void writeAttributes(ByteBuffer bb) {
		bb.addString(text);
	}

	@Override
	public String toString() {
		return getName() + "  text=" + text;
	}
}
