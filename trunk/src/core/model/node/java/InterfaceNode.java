package core.model.node.java;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.LinkedList;

import util.ByteBuffer;
import util.ByteReader;
import core.model.node.SceneNode;

public class InterfaceNode extends SceneNode {

	public static final int ID = 2;

	private String parentName, packageName, interfaceName;
	private final LinkedList<String> methods;

	public InterfaceNode() {
		parentName = "";
		packageName = "";
		setInterfaceName("Untitled");
		methods = new LinkedList<String>();
	}

	public InterfaceNode(int id, String name, Rectangle bounds) {
		super(id, name, bounds);
		methods = new LinkedList<String>();
	}

	public LinkedList<String> getMethods() {
		return methods;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
		super.setName("interface " + interfaceName);
	}

	@Override
	public Dimension getInitalSize() {
		return new Dimension(180, 100);
	}

	@Override
	public String getNodeType() {
		return "Interface";
	}

	@Override
	protected int getOutputID() {
		return ID;
	}

	@Override
	protected void readAttributes(ByteReader bb) {
		parentName = bb.readString();
		packageName = bb.readString();
		interfaceName = bb.readString();
		readList(methods, bb);
	}

	@Override
	protected void writeAttributes(ByteBuffer bb) {
		bb.addString(parentName);
		bb.addString(packageName);
		bb.addString(interfaceName);
		writeList(methods, bb);
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
