package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import core.model.node.SceneNode;

public class ConnectionRenderer {

	private final Stroke lineStroke = new BasicStroke(5.0f);
	//BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private final Color strokeColor = new Color(50, 50, 50);

	public void render(Graphics2D g, SceneNode a, SceneNode b, boolean ordered) {
		if (a.getBounds().intersects(b.getBounds()))
			return;
		if (a.getX() > b.getX()) {
			render(g, b, a, false);
			return;
		}
		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		//			RenderingHints.VALUE_ANTIALIAS_ON);
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
			drawLine(g, p.x, p.y, p2.x, p2.y, ordered);
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
			drawLine(g, p.x, p.y, p2.x, p2.y, ordered);
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
					drawLine(g, p.x, p.y, p2.x, p.y, ordered);
					drawLine(g, p2.x, p.y, p2.x, p2.y, ordered);

				} else {
					// bottom --> left
					p = getBottom(a);
					p2 = getLeft(b);
					drawLine(g, p.x, p.y, p.x, p2.y, ordered);
					drawLine(g, p.x, p2.y, p2.x, p2.y, ordered);
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
					drawLine(g, p.x, p.y, p.x, p2.y, ordered);
					drawLine(g, p.x, p2.y, p2.x, p2.y, ordered);
				} else {
					// right --> bottom
					p = getRight(a);
					p2 = getBottom(b);
					drawLine(g, p.x, p.y, p2.x, p.y, ordered);
					drawLine(g, p2.x, p.y, p2.x, p2.y, ordered);
				}
			}
		}
	}

	private void drawLine(Graphics2D g, double x, double y, double xx,
			double yy, boolean ordered) {
		if (ordered)
			drawArrow(g, (int) x, (int) y, (int) xx, (int) yy);
		else
			drawArrow(g, (int) xx, (int) yy, (int) x, (int) y);
	}

	int[] xPoints = new int[3];
	int[] yPoints = new int[3];
	float[] vecLine = new float[2];
	float[] vecLeft = new float[2];

	private void drawArrow(Graphics2D g, int x, int y, int xx, int yy) {
		float arrowWidth = 15.0f;
		float theta = 0.623f;

		float fLength;
		float th;
		float ta;
		float baseX, baseY;

		xPoints[0] = xx;
		yPoints[0] = yy;

		// build the line vector
		vecLine[0] = (float) xPoints[0] - x;
		vecLine[1] = (float) yPoints[0] - y;

		// build the arrow base vector - normal to the line
		vecLeft[0] = -vecLine[1];
		vecLeft[1] = vecLine[0];

		// setup length parameters
		fLength = (float) Math.sqrt(vecLine[0] * vecLine[0] + vecLine[1]
				* vecLine[1]);
		th = arrowWidth / (2.0f * fLength);
		ta = arrowWidth / (2.0f * ((float) Math.tan(theta) / 2.0f) * fLength);

		// find the base of the arrow
		baseX = (xPoints[0] - ta * vecLine[0]);
		baseY = (yPoints[0] - ta * vecLine[1]);

		// build the points on the sides of the arrow
		xPoints[1] = (int) (baseX + th * vecLeft[0]);
		yPoints[1] = (int) (baseY + th * vecLeft[1]);
		xPoints[2] = (int) (baseX - th * vecLeft[0]);
		yPoints[2] = (int) (baseY - th * vecLeft[1]);

		g.drawLine(x, y, (int) baseX, (int) baseY);
		g.fillPolygon(xPoints, yPoints, 3);
	}

	Point rightPoint = new Point(), topPoint = new Point(),
			bottomPoint = new Point(), leftPoint = new Point();

	private Point getRight(SceneNode a) {
		rightPoint.setLocation(a.getX() + a.getWidth(), a.getY()
				+ a.getHeight() / 2);
		return rightPoint;
	}

	private Point getTop(SceneNode a) {
		topPoint.setLocation(a.getX() + a.getWidth() / 2, a.getY());
		return topPoint;
	}

	private Point getBottom(SceneNode a) {
		bottomPoint.setLocation(a.getX() + a.getWidth() / 2, a.getY()
				+ a.getHeight());
		return bottomPoint;
	}

	private Point getLeft(SceneNode a) {
		leftPoint.setLocation(a.getX(), a.getY() + a.getHeight() / 2);
		return leftPoint;
	}

}
