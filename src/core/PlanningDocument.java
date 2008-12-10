package core;

import gui.ModelRenderer;
import gui.planning.LinkOptions;
import gui.planning.PlanningModelRenderer;
import gui.planning.TextOptions;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;

import javax.swing.JList;
import javax.swing.JPanel;

import core.model.PlanningSceneNodeSet;
import core.model.SceneNodeSet;
import core.model.UMLModel;
import core.model.node.SceneNode;
import core.model.node.planning.LinkNode;
import core.model.node.planning.TextNode;

public class PlanningDocument extends JinkDocument {

	private final LinkOptions linkOptionsGUI;
	private final TextOptions textOptionsGUI;

	public PlanningDocument(String title, JList stackList, JPanel optionsHolder) {
		this(title, stackList, optionsHolder,
				new LinkedHashMap<SceneNode, UMLModel>());
	}

	public PlanningDocument(String title, JList stackList,
			JPanel optionsHolder, LinkedHashMap<SceneNode, UMLModel> planeShifts) {
		super(title, stackList, optionsHolder, planeShifts);
		linkOptionsGUI = new LinkOptions(this);
		textOptionsGUI = new TextOptions(this);
	}

	@Override
	protected SceneNodeSet getSceneNodeSet() {
		return new PlanningSceneNodeSet();
	}

	@Override
	protected JPanel getOptionsFor(SceneNode node) {
		if (node instanceof LinkNode) {
			return linkOptionsGUI.init((LinkNode) node);
		} else if (node instanceof TextNode) {
			return textOptionsGUI.init((TextNode) node);
		}
		return super.getOptionsFor(node);
	}

	@Override
	protected ModelRenderer getModelRenderer() {
		return new PlanningModelRenderer(root);
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
		} else {
			super.activate(selected);
		}
	}

	@Override
	public int getTypeID() {
		return 0;
	}

}
