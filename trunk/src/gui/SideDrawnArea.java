package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;

import core.JinkDocument;
import core.model.SceneNodeSet;

public class SideDrawnArea extends JComponent {

	public SideDrawnArea(final JinkDocument controller, final SceneNodeSet set) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		LinkedList<String> list = set.getNodeNames();
		for (final String s : list) {
			add(Box.createVerticalStrut(5));
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
