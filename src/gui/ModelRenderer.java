package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import core.model.UMLModel;
import core.model.node.RecursiveSceneNode;
import core.model.node.SceneNode;

public class ModelRenderer {

	private static final Font font = new Font("Arial", Font.PLAIN, 14);
	private static final Color inner_fill = new Color(240, 240, 255),
			outer_border = Color.black, title_color = Color.black,
			grid_color = new Color(200, 200, 200), dividor_color = Color.black;
	private static final int GRID_SIZE = 20;
	public static boolean SHOW_GLIMPSES = true;
	protected UMLModel model;
	protected int width, height;
	private final LinkedList<SceneNode> dirty = new LinkedList<SceneNode>();
	private final ConnectionRenderer connectionRenderer = new ConnectionRenderer();
	private boolean showGrid = true;

	public ModelRenderer(UMLModel model) {
		this.model = model;
	}

	protected void drawNodes(Graphics2D g) {
		clean(g);
		Iterator<SceneNode> iter = model.getNodeIterator();
		while (iter.hasNext()) {
			SceneNode n = iter.next();
			render(n, g);
		}
	}

	protected void render(SceneNode n, Graphics2D g) {
		int x = n.getX(), y = n.getY();
		int w = n.getWidth(), h = n.getHeight();
		g.setColor(inner_fill);
		g.fillRect(x, y, w, h);
		g.setColor(title_color);
		g.setFont(font);
		drawCenteredString(g, n.getName(), x, y, w);
		g.setColor(dividor_color);
		g.drawLine(x, y + 16, x + w, y + 16);
		if (n instanceof RecursiveSceneNode) {
			if (SHOW_GLIMPSES) {
				BufferedImage bi = ((RecursiveSceneNode) n).getGlimpse();
				if (bi != null) {
					g
							.setRenderingHint(
									RenderingHints.KEY_INTERPOLATION,
									RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
					g.drawImage(bi, x, y + 16, w, h - 16, null);
				}
			}
		}
		g.setColor(outer_border);
		g.drawRect(x, y, w, h);
	}

	protected void drawLinks(Graphics2D g) {
		HashMap<SceneNode, HashSet<SceneNode>> graph = model.getGraph();
		for (SceneNode from : graph.keySet()) {
			HashSet<SceneNode> toSet = graph.get(from);
			for (SceneNode to : toSet) {
				connectionRenderer.render(g, from, to, true);
			}
		}
	}

	private void drawGrid(Graphics2D g, double w, double h, double px,
			double py, double zoom) {
		if (showGrid) {
			g.setColor(grid_color);
			if (zoom > 1) {
				w += zoom * zoom * GRID_SIZE;
				h += zoom * zoom * GRID_SIZE;
			} else {
				w += 5 * GRID_SIZE;
				h += 5 * GRID_SIZE;
			}
			w /= zoom;
			h /= zoom;
			double left = ((int) px) / GRID_SIZE * GRID_SIZE - GRID_SIZE;
			double up = ((int) py) / GRID_SIZE * GRID_SIZE - GRID_SIZE;
			for (double i = left; i <= w + left; i += GRID_SIZE) {
				g.drawLine((int) i, (int) up, (int) i, (int) (h + up));
			}
			for (double j = up; j < h + up; j += GRID_SIZE) {
				g.drawLine((int) left, (int) j, (int) (w + left), (int) j);
			}
		}
	}

	public void render(Graphics gg, double zoom, double panX, double panY,
			int w, int h) {
		Graphics2D g = (Graphics2D) gg;
		g.scale(zoom, zoom);
		g.translate(-panX, -panY);
		drawGrid(g, w, h, panX, panY, zoom);
		drawNodes(g);
		drawLinks(g);
		g.getTransform().setToIdentity();
	}

	protected void clean(Graphics g) {
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

	protected void drawCenteredString(Graphics g, String s, int x, int y,
			int width) {
		FontMetrics fm = g.getFontMetrics();
		int w = fm.stringWidth(s);
		int gap = (width - w) / 2;
		g.drawString(s, x + gap, y + fm.getAscent());
	}

	protected String getCutOffString(Graphics2D g, String s, int w,
			FontMetrics fm) {
		String dotDot = "..";
		int ddSize = fm.stringWidth(dotDot);
		StringBuilder sb = new StringBuilder(dotDot);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			sb = sb.insert(sb.length() - dotDot.length(), c);
			if (fm.stringWidth(sb.toString()) + ddSize > w) {
				return sb.deleteCharAt(sb.length() - dotDot.length() - 1)
						.toString();
			}
		}
		return sb.substring(0, sb.length() - dotDot.length());
	}

	public void dirty(SceneNode selected) {
		dirty.add(selected);
	}

	public void setModel(UMLModel currentModel) {
		this.model = currentModel;
		dirty.clear();
	}

	public void setShowGrid(boolean b) {
		showGrid = b;
	}

}
