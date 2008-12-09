package core.model.node.java;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.LinkedList;

import util.ByteBuffer;
import util.ByteReader;
import core.model.node.SceneNode;

public class ClassNode extends SceneNode {

	public static final int ID = 1;

	private String parentName, packageName, className;
	private final LinkedList<String> methods, fields;
	private boolean amAbstract;

	public ClassNode() {
		parentName = "Object";
		packageName = "";
		amAbstract = false;
		setClassName("Untitled");
		methods = new LinkedList<String>();
		fields = new LinkedList<String>();
	}

	public ClassNode(int id, String name, Rectangle bounds) {
		super(id, name, bounds);
		methods = new LinkedList<String>();
		fields = new LinkedList<String>();
	}

	public LinkedList<String> getMethods() {
		return methods;
	}

	public LinkedList<String> getFields() {
		return fields;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
		if (amAbstract)
			super.setName("abstract class " + className);
		else
			super.setName("class " + className);
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public Dimension getInitalSize() {
		return new Dimension(180, 180);
	}

	@Override
	public String getNodeType() {
		return "Class";
	}

	public void setAbstract(boolean b) {
		amAbstract = b;
		setClassName(className);
	}

	public boolean isAbstract() {
		return amAbstract;
	}

	@Override
	protected int getOutputID() {
		return ID;
	}

	@Override
	protected void readAttributes(ByteReader bb) {
		parentName = bb.readString();
		packageName = bb.readString();
		className = bb.readString();
		amAbstract = bb.readBoolean();
		readList(methods, bb);
		readList(fields, bb);
	}

	@Override
	protected void writeAttributes(ByteBuffer bb) {
		bb.addString(parentName);
		bb.addString(packageName);
		bb.addString(className);
		bb.addBoolean(amAbstract);
		writeList(methods, bb);
		writeList(fields, bb);
	}

	private void writeList(LinkedList<String> list, ByteBuffer bb) {
		bb.addShort((short) list.size());
		for (String s : list) {
			bb.addString(s);
		}
	}

	private void readList(LinkedList<String> list, ByteReader bb) {
		int len = bb.readShort();
		for (int i = 0; i < len; i++) {
			list.add(bb.readString());
		}
	}
}
