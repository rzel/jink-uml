package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import core.model.UMLModel;
import core.model.node.SceneNode;

public class ModelRenderer {

	private static final Font font = new Font("Arial", Font.PLAIN, 14);
	private static final Color inner_fill = new Color(240, 240, 255),
			outer_border = Color.black, title_color = Color.black,
			dividor_color = Color.black;
	private UMLModel model;
	protected int width, height;
	private final LinkedList<SceneNode> dirty = new LinkedList<SceneNode>();
	private final ConnectionRenderer connectionRenderer = new ConnectionRenderer();

	public ModelRenderer(UMLModel model) {
		this.model = model;
	}

	protected void drawBackground(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
	}

	protected void drawNodes(Graphics2D g) {
		clean(g);
		Iterator<SceneNode> iter = model.getNodeIterator();
		while (iter.hasNext()) {
			SceneNode n = iter.next();
			int x = n.getX(), y = n.getY();
			int w = n.getWidth(), h = n.getHeight();
			g.setColor(inner_fill);
			g.fillRect(x, y, w, h);
			g.setColor(outer_border);
			g.drawRect(x, y, w, h);
			g.setColor(title_color);
			g.setFont(font);
			drawCenteredString(g, n.getName(), x, y, w);
			g.setColor(dividor_color);
			g.drawLine(x, y + 16, x + w, y + 16);
		}
	}

	protected void drawLinks(Graphics2D g) {
		HashMap<SceneNode, HashSet<SceneNode>> graph = model.getGraph();
		for (SceneNode from : graph.keySet()) {
			HashSet<SceneNode> toSet = graph.get(from);
			for (SceneNode to : toSet) {
				connectionRenderer.render(g, from, to);
			}
		}
	}

	public void render(Graphics gg, int width, int height, double zoom,
			double panX, double panY) {
		Graphics2D g = (Graphics2D) gg;
		this.width = width;
		this.height = height;
		drawBackground(g);
		g.scale(zoom, zoom);
		g.translate(-panX, -panY);
		drawNodes(g);
		drawLinks(g);
		g.getTransform().setToIdentity();
	}

	private void clean(Graphics g) {
		for (SceneNode sn : dirty) {
			g.setFont(font);
			FontMetrics fm = g.getFontMetrics();
			int w = fm.stringWidth(sn.getName()) + 5;
			if (w > sn.getWidth()) {
				int dif = w - sn.getWidth();
				sn.setSize(w, sn.getHeight());
				sn.setLocation(sn.getX() - dif / 2, sn.getY());
			}
		}
		dirty.clear();
	}

	private void drawCenteredString(Graphics g, String s, int x, int y,
			int width) {
		FontMetrics fm = g.getFontMetrics();
		int w = fm.stringWidth(s);
		int gap = (width - w) / 2;
		g.drawString(s, x + gap, y + fm.getAscent());
	}

	public void dirty(SceneNode selected) {
		dirty.add(selected);
	}

	public void setModel(UMLModel currentModel) {
		this.model = currentModel;
		dirty.clear();
	}

}
