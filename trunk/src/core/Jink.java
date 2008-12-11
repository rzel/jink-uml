package core;

import gui.JinkGUI;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import util.Settings;
import core.io.JinkIO;

public class Jink {

	private final JinkGUI gui;
	private JinkDocument current = null;

	public Jink() {
		gui = new JinkGUI(this);
		gui.setVisible(true);
		gui.newChart();
	}

	public JinkDocument getCurrentDocument() {
		return current;
	}

	public void setCurrentDocument(JinkDocument jinkDocument) {
		this.current = jinkDocument;
		if (jinkDocument == null) {
			gui.setTitle("Jink UML Planner");
		} else {
			gui.setTitle("Jink UML Planner (" + current.getTitle() + ")");
		}
	}

	public void closeDocument() {
		if (current == null)
			return;
		if (current.hasChange()) {
			int i = JOptionPane.showConfirmDialog(gui,
					"Do you wish to save this document first?", "Save?",
					JOptionPane.YES_NO_OPTION);
			if (i == JOptionPane.YES_OPTION)
				save(current);
		}
		current = null;
	}

	public boolean save(JinkDocument jinkDocument) {
		File f = jinkDocument.getSaveLocation();
		if (f == null) {
			JFileChooser jfc = gui.getFileChooser();
			int ret = jfc.showSaveDialog(gui);
			if (ret == JOptionPane.CANCEL_OPTION)
				return false;
			f = jfc.getSelectedFile();
			if (f == null)
				return false;
			if (f.getName().endsWith(".jink") == false) {
				f = new File(f.getPath() + ".jink");
			}
		}
		try {
			jinkDocument.setTitle(f.getName());
			JinkIO.write(jinkDocument, f);
			jinkDocument.setSaveLoc(f);
			jinkDocument.setDirty(false);
			Settings.getCommonSettings().putSetting("last-loc", f.getPath());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		// System.setOut(new DebugOut());
		new Jink();
	}

}
