package gui.planning;

import gui.beans.LinkOptions_Beans;

import javax.swing.JPanel;

import core.JinkDocument;
import core.model.node.planning.LinkNode;

public class LinkOptions extends LinkOptions_Beans {

	private LinkNode editing;
	private final JinkDocument doc;

	public LinkOptions(JinkDocument doc) {
		this.doc = doc;
	}

	@Override
	protected void updateTitle(String newTitle) {
		editing.setName(newTitle);
		doc.notifyDirty(editing);
	}

	@Override
	protected void updateURL(String url) {
		editing.setURL(url);
		doc.notifyDirty(editing);
	}

	public void setEditing(LinkNode node) {
		this.editing = node;
	}

	public JPanel init(LinkNode node) {
		this.editing = node;
		super.titleField.setText(node.getName());
		super.urlField.setText(node.getURL());
		return this;
	}
}
