package core;

import gui.ModelRenderer;
import gui.planning.LinkOptions;
import gui.planning.PlanningModelRenderer;

import java.awt.Desktop;
import java.net.URI;
import java.util.LinkedHashMap;

import javax.swing.JList;
import javax.swing.JPanel;

import core.model.PlanningSceneNodeSet;
import core.model.SceneNodeSet;
import core.model.UMLModel;
import core.model.node.SceneNode;
import core.model.node.planning.LinkNode;

public class PlanningDocument extends JinkDocument {

	private final LinkOptions linkOptionsGUI;

	public PlanningDocument(String title, JList stackList, JPanel optionsHolder) {
		this(title, stackList, optionsHolder,
				new LinkedHashMap<SceneNode, UMLModel>());
	}

	public PlanningDocument(String title, JList stackList,
			JPanel optionsHolder, LinkedHashMap<SceneNode, UMLModel> planeShifts) {
		super(title, stackList, optionsHolder, planeShifts);
		linkOptionsGUI = new LinkOptions(this);
	}

	@Override
	protected SceneNodeSet getSceneNodeSet() {
		return new PlanningSceneNodeSet();
	}

	@Override
	protected JPanel getOptionsFor(SceneNode node) {
		if (node instanceof LinkNode) {
			return linkOptionsGUI.init((LinkNode) node);
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
			} catch (Exception e) {
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
