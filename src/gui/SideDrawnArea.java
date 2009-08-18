package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComponent;

import core.JinkDocument;
import core.model.SceneNodeSet;

public class SideDrawnArea extends JComponent {

	public SideDrawnArea(final JinkDocument controller, final SceneNodeSet set) {
		setMaximumSize(new Dimension(200, 10000));
		setPreferredSize(new Dimension(200, 100));
		setLayout(new FlowLayout());
		LinkedList<String> list = set.getNodeNames();
		for (final String s : list) {
			JButton jb = new JButton(s);
			jb.setAlignmentX(.5f);
			add(jb);
			jb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.setDragging(set.createNode(s));
				}
			});
		}
	}
}
