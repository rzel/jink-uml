package gui.planning;

import gui.beans.TextOptions_Beans;
import core.JinkDocument;
import core.model.node.planning.TextNode;

public class TextOptions extends TextOptions_Beans {

	private final JinkDocument doc;
	private TextNode editing;

	public TextOptions(JinkDocument doc) {
		this.doc = doc;
	}

	public TextOptions init(TextNode node) {
		this.editing = node;
		super.titleField.setText(node.getName());
		super.inputTextArea.setText(node.getText());
		return this;
	}

	@Override
	protected void updateText(String text) {
		editing.setText(text);
		doc.notifyDirty(editing);
	}

	@Override
	protected void updateTitle(String text) {
		editing.setName(text);
		doc.notifyDirty(editing);
	}

}
