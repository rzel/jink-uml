package gui.java;

import gui.beans.ClassOptions_Beans;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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

	private void editSelectedField() {
		int i = super.fieldList.getSelectedIndex();
		if (i >= 0) {
			Object ret = JOptionPane.showInputDialog(this,
					"Enter your changes:", "Edit Field",
					JOptionPane.PLAIN_MESSAGE, null, null, fieldModel.get(i));
			if (ret != null) {
				fieldModel.add(i, ret.toString());
				fieldModel.remove(i + 1);
				LinkedList<String> fields = editing.getFields();
				fields.add(i, ret.toString());
				fields.remove(i + 1);
			}
		}
		doc.notifyDirty(editing);
	}

	private void editSelectedMethod() {
		int i = super.methodList.getSelectedIndex();
		if (i >= 0) {
			Object ret = JOptionPane.showInputDialog(this,
					"Enter your changes:", "Edit Method",
					JOptionPane.PLAIN_MESSAGE, null, null, methodModel.get(i));
			if (ret != null) {
				methodModel.add(i, ret.toString());
				methodModel.remove(i + 1);
				LinkedList<String> methods = editing.getMethods();
				methods.add(i, ret.toString());
				methods.remove(i + 1);
			}
		}
		doc.notifyDirty(editing);
	}

	private void moveSelectedFieldsDown() {
		int[] indices = super.fieldList.getSelectedIndices();
		for (int j = indices.length - 1; j >= 0; j--) {
			int i = indices[j];
			if (i < fieldModel.getSize() - 1) {
				String txt = (String) fieldModel.get(i);
				fieldModel.remove(i);
				fieldModel.add(i + 1, txt);
				editing.getFields().remove(txt);
				editing.getFields().add(i + 1, txt);
				fieldList.addSelectionInterval(i + 1, i + 1);
			}
		}
		doc.notifyDirty(editing);
	}

	private void moveSelectedFieldsUp() {
		int[] indices = super.fieldList.getSelectedIndices();
		for (int j = 0; j < indices.length; j++) {
			int i = indices[j];
			if (i > 0) {
				String txt = (String) fieldModel.get(i);
				fieldModel.remove(i);
				fieldModel.add(i - 1, txt);
				editing.getFields().remove(txt);
				editing.getFields().add(i - 1, txt);
				fieldList.addSelectionInterval(i - 1, i - 1);
			}
		}
		doc.notifyDirty(editing);
	}

	private void moveSelectedMethodsDown() {
		int[] indices = super.methodList.getSelectedIndices();
		for (int j = indices.length - 1; j >= 0; j--) {
			int i = indices[j];
			if (i < methodModel.getSize() - 1) {
				String txt = (String) methodModel.get(i);
				methodModel.remove(i);
				methodModel.add(i + 1, txt);
				editing.getMethods().remove(txt);
				editing.getMethods().add(i + 1, txt);
				methodList.addSelectionInterval(i + 1, i + 1);
			}
		}
		doc.notifyDirty(editing);
	}

	private void moveSelectedMethodsUp() {
		int[] indices = super.methodList.getSelectedIndices();
		for (int j = 0; j < indices.length; j++) {
			int i = indices[j];
			if (i > 0) {
				String txt = (String) methodModel.get(i);
				methodModel.remove(i);
				methodModel.add(i - 1, txt);
				editing.getMethods().remove(txt);
				editing.getMethods().add(i - 1, txt);
				methodList.addSelectionInterval(i - 1, i - 1);
			}
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
		JMenuItem edit = new JMenuItem("Edit");
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editSelectedMethod();
			}
		});
		jpm.add(edit);
		JMenuItem up = new JMenuItem("Move Up");
		up.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveSelectedMethodsUp();
			}
		});
		jpm.add(up);
		JMenuItem down = new JMenuItem("Move Down");
		down.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveSelectedMethodsDown();
			}
		});
		jpm.add(down);
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
		JMenuItem edit = new JMenuItem("Edit");
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editSelectedField();
			}
		});
		jpm.add(edit);
		JMenuItem up = new JMenuItem("Move Up");
		up.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveSelectedFieldsUp();
			}
		});
		jpm.add(up);
		JMenuItem down = new JMenuItem("Move Down");
		down.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveSelectedFieldsDown();
			}
		});
		jpm.add(down);
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
