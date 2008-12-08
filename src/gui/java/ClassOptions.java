package gui.java;

import gui.beans.ClassOptions_Beans;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import core.JavaJinkDocument;
import core.model.node.java.ClassNode;

public class ClassOptions extends ClassOptions_Beans {

	private ClassNode editing;
	private final JavaJinkDocument doc;

	public ClassOptions(JavaJinkDocument doc) {
		this.doc = doc;
	}

	public void init(ClassNode node) {
		this.editing = node;
		super.superclassField.setText(editing.getParentName());
		super.packageField.setText(editing.getPackageName());
		super.classField.setText(editing.getClassName());
		int i = 0;
		if (editing.isAbstract())
			i = 1;
		abstractCombo.setSelectedIndex(i);
		methodModel.clear();
		fieldModel.clear();
		for (String m : editing.getMethods()) {
			methodModel.addElement(m);
		}
		for (String m : editing.getFields()) {
			fieldModel.addElement(m);
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

	private void deleteSelectedFields() {
		int[] indices = super.fieldList.getSelectedIndices();
		for (int j = indices.length - 1; j >= 0; j--) {
			int i = indices[j];
			String txt = (String) fieldModel.get(i);
			fieldModel.remove(i);
			editing.getFields().remove(txt);
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
	protected JPopupMenu getFieldPopupMenu() {
		JPopupMenu jpm = new JPopupMenu();
		JMenuItem remove = new JMenuItem("Delete");
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteSelectedFields();
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
		editing.setClassName(name);
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

	@Override
	protected void addField(String txt) {
		fieldModel.addElement(txt);
		editing.getFields().add(txt);
		doc.notifyDirty(editing);
	}

	@Override
	protected void setAbstract(boolean b) {
		editing.setAbstract(b);
		doc.notifyDirty(editing);
	}

}
