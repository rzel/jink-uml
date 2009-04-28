package core;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Instructions extends JFrame {

	private static final String HELP_TEXT = "Creating a Graph\n"
			+ "- Click on the type of node you want to add from the upper-right corner.  Then click where you want to place the node.\n"
			+ "- To add a link between two nodes, left-click and drag from one node to another.\n"
			+ "- To remove all links from a node, middle-click that node.\n"
			+ "- To move a node, right-click on it and drag.\n"
			+ "- To resize a node, click on it and then drag one of the handles on the edge of the node.\n\n"
			+ "Navigating the Graph\n"
			+ "- To scroll, use the arrow keys (or WASD).\n"
			+ "- To zoom in and out, use the mouse wheel.\n"
			+ "- To create a new graph inside an existing node, zoom into it.\n"
			+ "- If you are inside one of your nodes, zoom far out to go up to the previous level.\n"
			+ "- To 'activate' a node, double-click it.  The effect is based on the type of node."
			+ "  The default behavior is that you will zoom into that node.\n"
			+ "- You can also move up in depth by clicking on the stack in the bottom-right corner.";

	private static final Instructions single = new Instructions();

	private Instructions() {
		super("Instructions");
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setResizable(false);
		setContentPane(createContent());
		setSize(500, 300);
	}

	private JComponent createContent() {
		JPanel container = new JPanel(new GridLayout(1, 1));
		int gap = 5;
		container
				.setBorder(BorderFactory.createEmptyBorder(gap, gap, gap, gap));
		JTextArea jta = new JTextArea();
		jta.setBackground(container.getBackground());
		jta.setHighlighter(null);
		jta.setEditable(false);
		jta.setWrapStyleWord(true);
		jta.setLineWrap(true);
		jta.setText(HELP_TEXT);
		container.add(jta);
		return container;
	}

	public static void showInstructions() {
		single.setLocationRelativeTo(null);
		single.setVisible(true);
	}

}
