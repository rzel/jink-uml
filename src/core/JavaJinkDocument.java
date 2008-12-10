package core;

import gui.ModelRenderer;
import gui.java.ClassOptions;
import gui.java.InterfaceOptions;
import gui.java.JavaModelRenderer;
import gui.planning.LinkOptions;
import gui.planning.TextOptions;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;

import javax.swing.JList;
import javax.swing.JPanel;

import core.model.JavaSceneNodeSet;
import core.model.SceneNodeSet;
import core.model.UMLModel;
import core.model.node.SceneNode;
import core.model.node.java.ClassNode;
import core.model.node.java.InterfaceNode;
import core.model.node.planning.LinkNode;
import core.model.node.planning.TextNode;

public class JavaJinkDocument extends JinkDocument {

	private final ClassOptions classOptionsGUI;
	private final InterfaceOptions interfaceOptionsGUI;
	private final LinkOptions linkOptionsGUI;
	private final TextOptions textOptionsGUI;

	public JavaJinkDocument(String title, JList stackList, JPanel optionsHolder) {
		this(title, stackList, optionsHolder,
				new LinkedHashMap<SceneNode, UMLModel>());
	}

	public JavaJinkDocument(String title, JList stackList,
			JPanel optionsHolder, LinkedHashMap<SceneNode, UMLModel> planeShifts) {
		super(title, stackList, optionsHolder, planeShifts);
		classOptionsGUI = new ClassOptions(this);
		interfaceOptionsGUI = new InterfaceOptions(this);
		linkOptionsGUI = new LinkOptions(this);
		textOptionsGUI = new TextOptions(this);
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
		} else if (node instanceof LinkNode) {
			return linkOptionsGUI.init((LinkNode) node);
		} else if (node instanceof TextNode) {
			return textOptionsGUI.init((TextNode) node);
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

	@Override
	public void activate(SceneNode selected) {
		if (selected instanceof LinkNode) {
			LinkNode link = (LinkNode) selected;
			Desktop d = Desktop.getDesktop();
			try {
				d.browse(new URI(link.getURL()));
			} catch (URISyntaxException e) {
				System.err.println("Invalid URL: " + link.getURL());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (selected instanceof InterfaceNode
				|| selected instanceof ClassNode) {
			renderedPanel.clearSelection();
			zoomInto(selected);
		} else {
			super.activate(selected);
		}
	}
}
