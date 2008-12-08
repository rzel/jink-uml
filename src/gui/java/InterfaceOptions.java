package gui.java;

import gui.beans.InterfaceOptions_Beans;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import core.JavaJinkDocument;
import core.model.node.java.InterfaceNode;

public class InterfaceOptions extends InterfaceOptions_Beans {

	private InterfaceNode editing;
	private final JavaJinkDocument doc;

	public InterfaceOptions(JavaJinkDocument doc) {
		this.doc = doc;
	}

	public void init(InterfaceNode node) {
		this.editing = node;
		super.superclassField.setText(editing.getParentName());
		super.packageField.setText(editing.getPackageName());
		super.classField.setText(editing.getInterfaceName());
		methodModel.clear();
		fieldModel.clear();
		for (String m : editing.getMethods()) {
			methodModel.addElement(m);
		}
	}

	private void deleteSelectedMethods() {
		int[] indices = super.methodList.getSelectedIndices();
		for (int j = indices.length - 1; j >= 0; j--) {
			int i = indices[j];
			String txt = (String) methodModel.get(i);
			methodModel.remove(i);
			editing.getMethods().remove(txt);
		}
		doc.notifyDirty(editing);
	}

	@Override
	protected JPopupMenu getMethodPopupMenu() {
		JPopupMenu jpm = new JPopupMenu();
		JMenuItem remove = new JMenuItem("Delete");
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteSelectedMethods();
			}
		});
		jpm.add(remove);
		return jpm;
	}

	@Override
	protected void addMethod(String txt) {
		methodModel.addElement(txt);
		editing.getMethods().add(txt);
		doc.notifyDirty(editing);
	}

	@Override
	protected void updateClassName(String name) {
		editing.setInterfaceName(name);
		doc.notifyDirty(editing);
	}

	@Override
	protected void updatePackage(String name) {
		editing.setPackageName(name);
		doc.notifyDirty(editing);
	}

	@Override
	protected void updateParent(String name) {
		editing.setParentName(name);
		doc.notifyDirty(editing);
	}

}
