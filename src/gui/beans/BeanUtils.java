package gui.beans;

import javax.swing.JComponent;

public class BeanUtils {

	public static void setInner(JComponent parent, JComponent child) {
		parent.removeAll();
		if (child != null)
			parent.add(child);
		parent.validate();
		parent.repaint();
	}

}
