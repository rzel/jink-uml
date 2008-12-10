package core;

import gui.MainDrawnArea;
import gui.ModelRenderer;
import gui.NodeOptions;
import gui.SideDrawnArea;
import gui.beans.BeanUtils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

	private boolean hasChange = false;

	private File saveLoc;
	private final String title;
	protected final UMLModel root;
	private UMLModel currentModel;
	private final LinkedHashMap<SceneNode, UMLModel> planeShifts;
	private final Stack<UMLModel> stack = new Stack<UMLModel>();
	private final ModelRenderer renderer;
	private final MainDrawnArea renderedPanel;
	private final SideDrawnArea sidePanel;
	private final SceneNodeSet nodeSet;
	private final JList stackList;
	private final DefaultListModel stackListModel;
	private boolean listLocked = false;
	private final JPanel optionsHolder;

	public JinkDocument(String title, JList stackList, JPanel optionsHolder,
			LinkedHashMap<SceneNode, UMLModel> planeShifts) {
		this.stackList = stackList;
		this.stackListModel = (DefaultListModel) stackList.getModel();
		this.title = title;
		this.optionsHolder = optionsHolder;
		this.planeShifts = planeShifts;
		if (planeShifts.size() == 0) {
			root = new UMLModel();
			RecursiveSceneNode rsn = new RecursiveSceneNode();
			rsn.setName("root");
			planeShifts.put(rsn, root);
		} else
			root = planeShifts.values().iterator().next();
		currentModel = root;
		nodeSet = getSceneNodeSet();
		sidePanel = new SideDrawnArea(this, nodeSet);
		stackListModel.clear();
		stackListModel.addElement(SceneNode.ROOT_NODE);
		stackList.setSelectedIndex(0);
		renderer = getModelRenderer();
		renderedPanel = new MainDrawnArea(this, renderer);
		renderedPanel.centerOn(currentModel.getBounds());
	}

	protected abstract ModelRenderer getModelRenderer();

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
		renderedPanel.centerOn(currentModel.getBounds());
		listLocked = false;
	}

	public void zoomOut() {
		if (currentModel == root) {
			return;
		}
		applyGlimpse(currentModel);
		listLocked = true;
		currentModel = stack.pop();
		renderer.setModel(currentModel);
		stackListModel.remove(stackListModel.getSize() - 1);
		stackList.setSelectedIndex(stackListModel.getSize() - 1);
		renderedPanel.centerOn(currentModel.getBounds());
		listLocked = false;
	}

	private void applyGlimpse(final UMLModel model) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				for (SceneNode sn : planeShifts.keySet()) {
					if (planeShifts.get(sn) == model) {
						if (sn instanceof RecursiveSceneNode) {
							RecursiveSceneNode rsn = (RecursiveSceneNode) sn;
							BufferedImage bi = new BufferedImage(300, 300,
									BufferedImage.TYPE_INT_RGB);
							Graphics2D g = (Graphics2D) bi.getGraphics();
							g
									.setRenderingHint(
											RenderingHints.KEY_INTERPOLATION,
											RenderingHints.VALUE_INTERPOLATION_BILINEAR);
							g.setColor(new Color(240, 240, 255));
							g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
							ModelRenderer mr = getModelRenderer();
							mr.setShowGrid(false);
							mr.setModel(model);
							mr.render(g, .2, 0, 0, 0, 0);
							g.dispose();
							rsn.setGlimpse(bi);
							renderedPanel.repaint();
						}
						return;
					}
				}
			}
		});
		t.setPriority(Thread.MIN_PRIORITY);
		t.setDaemon(true);
		t.start();
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
		hasChange = true;
	}

	protected JPanel getOptionsFor(SceneNode node) {
		if (node instanceof RecursiveSceneNode) {
			return new NodeOptions((RecursiveSceneNode) node, this);
		}
		return null;
	}

	public void setAlignGrid(boolean b) {
		renderedPanel.setAlignGrid(b);
	}

	public void setShowGrid(boolean b) {
		renderer.setShowGrid(b);
		renderedPanel.repaint();
	}

	public HashMap<SceneNode, UMLModel> getPlaneShifts() {
		return planeShifts;
	}

	public File getSaveLocation() {
		return saveLoc;
	}

	public void setSaveLoc(File loc) {
		saveLoc = loc;
	}

	public void setDirty(boolean b) {
		hasChange = b;
	}

	public void activate(SceneNode selected) {
		if (selected instanceof RecursiveSceneNode) {
			renderedPanel.clearSelection();
			zoomInto(selected);
		}
	}

	public abstract int getTypeID();

}
