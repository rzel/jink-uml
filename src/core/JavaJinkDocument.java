package core;

import gui.ModelRenderer;
import gui.java.ClassOptions;
import gui.java.InterfaceOptions;
import gui.java.JavaModelRenderer;

import java.util.LinkedHashMap;

import javax.swing.JList;
import javax.swing.JPanel;

import core.model.JavaSceneNodeSet;
import core.model.SceneNodeSet;
import core.model.UMLModel;
import core.model.node.SceneNode;
import core.model.node.java.ClassNode;
import core.model.node.java.InterfaceNode;

public class JavaJinkDocument extends JinkDocument {

	private final ClassOptions classOptionsGUI;
	private final InterfaceOptions interfaceOptionsGUI;

	public JavaJinkDocument(String title, JList stackList, JPanel optionsHolder) {
		this(title, stackList, optionsHolder,
				new LinkedHashMap<SceneNode, UMLModel>());
	}

	public JavaJinkDocument(String title, JList stackList,
			JPanel optionsHolder, LinkedHashMap<SceneNode, UMLModel> planeShifts) {
		super(title, stackList, optionsHolder, planeShifts);
		classOptionsGUI = new ClassOptions(this);
		interfaceOptionsGUI = new InterfaceOptions(this);
	}

	@Override
	protected SceneNodeSet getSceneNodeSet() {
		return new JavaSceneNodeSet();
	}

	@Override
	protected JPanel getOptionsFor(SceneNode node) {
		if (node instanceof ClassNode) {
			classOptionsGUI.init((ClassNode) node);
			return classOptionsGUI;
		} else if (node instanceof InterfaceNode) {
			interfaceOptionsGUI.init((InterfaceNode) node);
			return interfaceOptionsGUI;
		}
		return super.getOptionsFor(node);
	}

	@Override
	protected ModelRenderer getModelRenderer() {
		return new JavaModelRenderer(root);
	}

	@Override
	public int getTypeID() {
		return 1;
	}

}
