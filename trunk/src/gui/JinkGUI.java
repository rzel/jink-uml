package gui;

import gui.beans.BeanUtils;
import gui.beans.JinkGUI_Beans;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import util.JinkFileFilter;
import core.JavaJinkDocument;
import core.Jink;
import core.JinkDocument;
import core.io.JinkIO;
import core.model.node.SceneNode;

public class JinkGUI extends JinkGUI_Beans implements ListSelectionListener {

	private static int COUNTER = 0;

	private final Jink controller;
	private DefaultListModel stackModel;
	private About about;
	private JFileChooser jfc;

	public JinkGUI(Jink controller) {
		this.controller = controller;
		super.setExtendedState(JFrame.MAXIMIZED_BOTH);
		super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		super.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				try {
					onShutdownHook();
				} catch (Exception ee) {
					ee.printStackTrace();
				}
				System.exit(0);
			}

		});
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
		controller.closeDocument();
		jfc = getFileChooser();
		int i = jfc.showOpenDialog(this);
		if (i == JFileChooser.CANCEL_OPTION)
			return;
		File f = jfc.getSelectedFile();
		try {
			JinkDocument jd = JinkIO.read(f, super.stackList,
					super.nodeInfoHolder);
			jd.setSaveLoc(f);
			controller.setCurrentDocument(jd);
			BeanUtils.setInner(super.mainAreaContainer, jd
					.getMainRenderedPanel());
			BeanUtils.setInner(super.sideBarContainer, jd
					.getSideRenderedPanel());
			sideBarContainer.revalidate();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					"The file you tried to open is either invalid or corrupt.");
		}
	}

	@Override
	protected void saveChart() {
		controller.save(controller.getCurrentDocument());
	}

	@Override
	protected void setAlignGrid(boolean b) {
		controller.getCurrentDocument().setAlignGrid(b);
	}

	@Override
	protected void setShowGrid(boolean b) {
		controller.getCurrentDocument().setShowGrid(b);
	}

	@Override
	protected void showAbout() {
		if (about == null) {
			about = new About();
			about.setVisible(true);
		}
	}

	private void onShutdownHook() {
		controller.closeDocument();
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

	public JFileChooser getFileChooser() {
		if (jfc == null) {
			jfc = new JFileChooser();
			jfc.setFileFilter(new JinkFileFilter());
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		return jfc;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		SceneNode sn = (SceneNode) stackList.getSelectedValue();
		if (controller.getCurrentDocument() != null)
			controller.getCurrentDocument().stackPressed(sn);
	}

	@Override
	protected void setShowGlimpse(boolean b) {
		ModelRenderer.SHOW_GLIMPSES = b;
		controller.getCurrentDocument().getMainRenderedPanel().repaint();
	}

}
