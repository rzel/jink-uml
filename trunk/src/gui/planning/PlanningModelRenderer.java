package gui.planning;

import gui.ModelRenderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import core.model.UMLModel;
import core.model.node.SceneNode;
import core.model.node.planning.LinkNode;

public class PlanningModelRenderer extends ModelRenderer {

	private static final Font link_font = new Font("Arial", Font.ITALIC, 14);
	private static final Color outer_border = Color.black,
			title_color = Color.black, dividor_color = Color.black,
			link_fill = new Color(255, 240, 240);

	public PlanningModelRenderer(UMLModel model) {
		super(model);
	}

	@Override
	protected void render(SceneNode n, Graphics2D g) {
		int x = n.getX(), y = n.getY();
		int w = n.getWidth(), h = n.getHeight();
		if (n instanceof LinkNode) {
			LinkNode c = (LinkNode) n;
			g.setColor(link_fill);
			g.fillRect(x, y, w, h);
			g.setColor(outer_border);
			g.drawRect(x, y, w, h);
			g.setColor(title_color);
			g.setFont(link_font);
			drawCenteredString(g, n.getName(), x, y, w);
			g.setColor(dividor_color);
			g.drawLine(x, y + 16, x + w, y + 16);
			FontMetrics fm = g.getFontMetrics();
			String s = getCutOffString(g, c.getURL(), w, fm);
			g.drawString(s, x + 2, y + 16 + fm.getAscent());
		} else {
			super.render(n, g);
		}
	}

}
