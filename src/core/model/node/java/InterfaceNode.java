package core.model.node.java;

import java.awt.Dimension;
import java.util.LinkedList;

import core.model.node.SceneNode;

public class InterfaceNode extends SceneNode {

	private String parentName, packageName, interfaceName;
	private final LinkedList<String> methods;

	public InterfaceNode() {
		parentName = "";
		packageName = "";
		setInterfaceName("Untitled");
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
		super.setName(interfaceName);
	}

	@Override
	public Dimension getInitalSize() {
		return new Dimension(100, 100);
	}

	@Override
	public String getNodeType() {
		return "Interface";
	}
}
