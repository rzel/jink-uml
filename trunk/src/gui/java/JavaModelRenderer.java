package gui.java;

import gui.ModelRenderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.LinkedList;

import core.model.UMLModel;
import core.model.node.SceneNode;
import core.model.node.java.ClassNode;
import core.model.node.java.InterfaceNode;

public class JavaModelRenderer extends ModelRenderer {

	private static final Font class_font = new Font("Arial", Font.PLAIN, 14),
			interface_font = new Font("Arial", Font.ITALIC, 14),
			stringsFont = class_font;
	private static final Color abstract_color = new Color(255, 240, 225),
			class_fill = new Color(255, 210, 195), outer_border = Color.black,
			title_color = Color.black, dividor_color = Color.black,
			interface_fill = new Color(240, 255, 240);

	public JavaModelRenderer(UMLModel model) {
		super(model);
	}

	@Override
	protected void render(SceneNode n, Graphics2D g) {
		int x = n.getX(), y = n.getY();
		int w = n.getWidth(), h = n.getHeight();
		if (n instanceof ClassNode) {
			ClassNode c = (ClassNode) n;
			if (c.isAbstract())
				g.setColor(abstract_color);
			else
				g.setColor(class_fill);
			g.fillRect(x, y, w, h);
			g.setColor(outer_border);
			g.drawRect(x, y, w, h);
			g.setColor(title_color);
			g.setFont(class_font);
			drawCenteredString(g, n.getName(), x, y, w);
			g.setColor(dividor_color);
			g.drawLine(x, y + 16, x + w, y + 16);
			double percentFields;
			if (c.getFields().size() == 0 && c.getMethods().size() == 0)
				percentFields = .5;
			else {
				int f = c.getFields().size();
				int m = c.getMethods().size();
				percentFields = 1.0 * f / (m + f);
				if (percentFields < .1)
					percentFields = .1;
				if (percentFields > .9)
					percentFields = .9;
			}
			int splitLoc = (int) (y + (h - 16) * percentFields + 16);
			g.drawLine(x, splitLoc, x + w, splitLoc);
			drawStringsInBox(g, x, y + 16, w, splitLoc - (y + 16), c
					.getFields());
			drawStringsInBox(g, x, splitLoc, w, y + h - splitLoc, c
					.getMethods());
		} else if (n instanceof InterfaceNode) {
			InterfaceNode c = (InterfaceNode) n;
			g.setColor(interface_fill);
			g.fillRect(x, y, w, h);
			g.setColor(outer_border);
			g.drawRect(x, y, w, h);
			g.setColor(title_color);
			g.setFont(interface_font);
			drawCenteredString(g, n.getName(), x, y, w);
			g.setColor(dividor_color);
			g.drawLine(x, y + 16, x + w, y + 16);
			g.setFont(stringsFont);
			drawStringsInBox(g, x, y + 16, w, h - 16, c.getMethods());
		} else {
			super.render(n, g);
		}
	}

	private void drawStringsInBox(Graphics2D g, int x, int y, int w, int h,
			LinkedList<String> list) {
		Iterator<String> iter = list.iterator();
		FontMetrics fm = g.getFontMetrics();
		int toY = y + h;
		int stringHeight = fm.getHeight();
		while (y + stringHeight < toY && iter.hasNext()) {
			String next = iter.next();
			next = getCutOffString(g, next, w, fm);
			g.drawString(next, x + 2, y + fm.getAscent());
			y += stringHeight;
		}
	}

}
