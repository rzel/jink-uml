package core;

import gui.JinkGUI;

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
		if (current.hasChange())
			save(current);
		current = null;
	}

	public void save(JinkDocument jinkDocument) {

	}

	public static void main(String[] args) {
		new Jink();
	}

}
