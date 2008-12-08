package core;

import gui.MainDrawnArea;
import gui.ModelRenderer;
import gui.NodeOptions;
import gui.SideDrawnArea;
import gui.beans.BeanUtils;

import java.util.HashMap;
import java.util.Stack;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;

import core.model.SceneNodeSet;
import core.model.UMLModel;
import core.model.node.RecursiveSceneNode;
import core.model.node.SceneNode;

public abstract class JinkDocument {

	private final boolean hasChange = true;

	private final String title;
	private final UMLModel root;
	private UMLModel currentModel;
	private final HashMap<SceneNode, UMLModel> planeShifts = new HashMap<SceneNode, UMLModel>();
	private final Stack<UMLModel> stack = new Stack<UMLModel>();
	private final ModelRenderer renderer;
	private final MainDrawnArea renderedPanel;
	private final SideDrawnArea sidePanel;
	private final SceneNodeSet nodeSet;
	private final JList stackList;
	private final DefaultListModel stackListModel;
	private boolean listLocked = false;
	private final JPanel optionsHolder;

	public JinkDocument(String title, JList stackList, JPanel optionsHolder) {
		this.stackList = stackList;
		this.stackListModel = (DefaultListModel) stackList.getModel();
		this.title = title;
		this.optionsHolder = optionsHolder;
		root = new UMLModel();
		currentModel = root;
		renderer = new ModelRenderer(root);
		renderedPanel = new MainDrawnArea(this, renderer);
		nodeSet = getSceneNodeSet();
		sidePanel = new SideDrawnArea(this, nodeSet);
		stackListModel.clear();
		RecursiveSceneNode rsn = new RecursiveSceneNode();
		rsn.setName("root");
		planeShifts.put(rsn, root);
		stackListModel.addElement(rsn);
		stackList.setSelectedIndex(0);
	}

	public void zoomInto(SceneNode into) {
		listLocked = true;
		UMLModel nextPlane = planeShifts.get(into);
		if (nextPlane == null) {
			nextPlane = new UMLModel();
			planeShifts.put(into, nextPlane);
		}
		stackListModel.addElement(into);
		stack.push(currentModel);
		currentModel = nextPlane;
		renderer.setModel(currentModel);
		stackList.setSelectedIndex(stackListModel.getSize() - 1);
		listLocked = false;
	}

	public void zoomOut() {
		if (currentModel == root) {
			return;
		}
		listLocked = true;
		currentModel = stack.pop();
		renderer.setModel(currentModel);
		stackListModel.remove(stackListModel.getSize() - 1);
		stackList.setSelectedIndex(stackListModel.getSize() - 1);
		listLocked = false;
	}

	public boolean hasChange() {
		return hasChange;
	}

	public ModelRenderer getRenderer() {
		return renderer;
	}

	public JComponent getMainRenderedPanel() {
		return renderedPanel;
	}

	public JComponent getSideRenderedPanel() {
		return sidePanel;
	}

	public UMLModel getModel() {
		return currentModel;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return getTitle();
	}

	protected abstract SceneNodeSet getSceneNodeSet();

	public void setDragging(SceneNode n) {
		renderedPanel.dragging = n;
	}

	public boolean isAtRoot() {
		return currentModel == root;
	}

	public void stackPressed(SceneNode sn) {
		if (listLocked)
			return;
		UMLModel to = planeShifts.get(sn);
		while (currentModel != to)
			zoomOut();
		renderedPanel.repaint();
	}

	public void setSelectedNode(SceneNode selected) {
		JPanel options = getOptionsFor(selected);
		BeanUtils.setInner(optionsHolder, options);
	}

	public void notifyDirty(SceneNode node) {
		renderer.dirty(node);
		renderedPanel.repaint();
	}

	protected JPanel getOptionsFor(SceneNode node) {
		if (node instanceof RecursiveSceneNode) {
			return new NodeOptions((RecursiveSceneNode) node, this);
		}
		return null;
	}

}
