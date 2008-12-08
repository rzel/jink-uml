package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import core.model.node.SceneNode;

public class ConnectionRenderer {

	private final Stroke lineStroke = new BasicStroke(5.0f,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private final Color strokeColor = new Color(0, 0, 0, 100);

	public void render(Graphics2D g, SceneNode a, SceneNode b) {
		if (a.getBounds().intersects(b.getBounds()))
			return;
		if (a.getX() > b.getX()) {
			render(g, b, a);
			return;
		}
		int acx = a.getX() + a.getWidth() / 2, acy = a.getY() + a.getHeight()
				/ 2;
		int bcx = b.getX() + b.getWidth() / 2, bcy = b.getY() + b.getHeight()
				/ 2;
		g.setColor(strokeColor);
		g.setStroke(lineStroke);
		if (a.getX() + a.getWidth() >= b.getX()
				&& a.getX() < b.getX() + b.getWidth()) {
			Point p, p2;
			if (a.getY() < b.getY()) {
				p = getBottom(a);
				p2 = getTop(b);
			} else {
				p = getTop(a);
				p2 = getBottom(b);
			}
			g.drawLine(p.x, p.y, p2.x, p2.y);
		} else if (a.getY() + a.getHeight() >= b.getY()
				&& a.getY() < b.getY() + b.getHeight()) {
			Point p, p2;
			if (a.getX() < b.getX()) {
				p = getRight(a);
				p2 = getLeft(b);
			} else {
				p = getLeft(a);
				p2 = getRight(b);
			}
			g.drawLine(p.x, p.y, p2.x, p2.y);
		} else {
			if (a.getY() < b.getY()) {
				// **X**
				// ***Y*
				double slope = 1.0 * (acy - bcy) / (bcx - acx);
				Point p, p2;
				if (slope > -1) {
					// right --> top
					p = getRight(a);
					p2 = getTop(b);
					g.drawLine(p.x, p.y, p2.x, p.y);
					g.drawLine(p2.x, p.y, p2.x, p2.y);

				} else {
					// bottom --> left
					p = getBottom(a);
					p2 = getLeft(b);
					g.drawLine(p.x, p.y, p.x, p2.y);
					g.drawLine(p.x, p2.y, p2.x, p2.y);
				}
			} else {
				// ***Y*
				// **X**
				double slope = 1.0 * (acy - bcy) / (bcx - acx);
				Point p, p2;
				if (slope < 1) {
					// top --> right
					p = getTop(a);
					p2 = getLeft(b);
					g.drawLine(p.x, p.y, p.x, p2.y);
					g.drawLine(p.x, p2.y, p2.x, p2.y);
				} else {
					// right --> bottom
					p = getRight(a);
					p2 = getBottom(b);
					g.drawLine(p.x, p.y, p2.x, p.y);
					g.drawLine(p2.x, p.y, p2.x, p2.y);
				}
			}
		}
	}

	private Point getRight(SceneNode a) {
		return new Point(a.getX() + a.getWidth(), a.getY() + a.getHeight() / 2);
	}

	private Point getTop(SceneNode a) {
		return new Point(a.getX() + a.getWidth() / 2, a.getY());
	}

	private Point getBottom(SceneNode a) {
		return new Point(a.getX() + a.getWidth() / 2, a.getY() + a.getHeight());
	}

	private Point getLeft(SceneNode a) {
		return new Point(a.getX(), a.getY() + a.getHeight() / 2);
	}

}
