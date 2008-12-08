package core.model.node.java;

import java.awt.Dimension;
import java.util.LinkedList;

import core.model.node.SceneNode;

public class ClassNode extends SceneNode {

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
		return new Dimension(100, 100);
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
}
