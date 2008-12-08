package gui;

import gui.beans.BeanUtils;
import gui.beans.JinkGUI_Beans;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import core.JavaJinkDocument;
import core.Jink;
import core.JinkDocument;
import core.model.node.SceneNode;

public class JinkGUI extends JinkGUI_Beans implements ListSelectionListener {

	private static int COUNTER = 0;

	private final Jink controller;
	private DefaultListModel stackModel;

	public JinkGUI(Jink controller) {
		this.controller = controller;
		super.setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				onShutdownHook();
			}
		}));
		super.stackList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		super.stackList.addListSelectionListener(this);
	}

	@Override
	public void newChart() {
		controller.closeDocument();
		JinkDocument newDocument = new JavaJinkDocument("Untitled "
				+ (++COUNTER), super.stackList, super.nodeInfoHolder);
		controller.setCurrentDocument(newDocument);
		BeanUtils.setInner(super.mainAreaContainer, newDocument
				.getMainRenderedPanel());
		BeanUtils.setInner(super.sideBarContainer, newDocument
				.getSideRenderedPanel());
		sideBarContainer.revalidate();
	}

	@Override
	protected void loadChart() {
		JOptionPane.showMessageDialog(null, "Not Implemented.");
	}

	@Override
	protected void saveChart() {
		JOptionPane.showMessageDialog(null, "Not Implemented.");

	}

	@Override
	protected void setAlignGrid(boolean b) {
		JOptionPane.showMessageDialog(null, "Not Implemented.");

	}

	@Override
	protected void setShowGrid(boolean b) {
		JOptionPane.showMessageDialog(null, "Not Implemented.");

	}

	@Override
	protected void showAbout() {
		JOptionPane.showMessageDialog(null, "Not Implemented.");

	}

	private void onShutdownHook() {
	}

	@Override
	protected void exit() {
		System.exit(0);
	}

	@Override
	protected DefaultListModel getStackModel() {
		if (stackModel == null)
			stackModel = new DefaultListModel();
		return stackModel;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		SceneNode sn = (SceneNode) stackList.getSelectedValue();
		if (controller.getCurrentDocument() != null)
			controller.getCurrentDocument().stackPressed(sn);
	}

}
