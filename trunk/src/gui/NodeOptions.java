package gui;

import gui.beans.NodeOptions_Beans;
import core.JinkDocument;
import core.model.node.RecursiveSceneNode;

public class NodeOptions extends NodeOptions_Beans {

	private final RecursiveSceneNode editing;
	private final JinkDocument doc;

	public NodeOptions(RecursiveSceneNode editing, JinkDocument doc) {
		this.editing = editing;
		this.doc = doc;
		super.titleField.setText(editing.getName());
	}

	@Override
	protected void updateTitle(String newTitle) {
		editing.setName(newTitle);
		doc.notifyDirty(editing);
	}

}
