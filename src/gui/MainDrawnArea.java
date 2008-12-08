package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JComponent;

import core.JinkDocument;
import core.model.node.SceneNode;

public class MainDrawnArea extends JComponent {

	private static final Color shadow = new Color(0, 0, 0, 50),
			dragger_color = new Color(0, 0, 0, 100), arrow_color = new Color(0,
					255, 0, 100), shadow_outline = Color.gray,
			arrow_to_color = new Color(255, 100, 0, 100);
	private static final int DRAGGER_SIZE = 9;
	private static final double ZOOM_MIN = .01, ZOOM_MAX = 20;

	private final JinkDocument controller;
	private final ModelRenderer renderer;
	private int offX, offY;
	private int mouseX, mouseY;
	private boolean mouseIn = false;
	boolean mouseDown = false;
	boolean drawingArrow = false;
	public SceneNode dragging, selected, arrowTo;
	private int drag_x, drag_y;
	private double zoom = 1;
	private double panX = 20;
	private double panY = 0;

	public MainDrawnArea(JinkDocument controller, ModelRenderer renderer) {
		this.controller = controller;
		this.renderer = renderer;
		this.addMouseListener(adapter);
		this.addMouseMotionListener(adapter);
		this.addMouseWheelListener(adapter);
		this.addKeyListener(keyAdapter);
	}

	@Override
	protected void paintComponent(Graphics g) {
		renderer.render(g, getWidth(), getHeight(), zoom, panX, panY);
		if (mouseIn) {
			if (dragging != null) {
				g.setColor(shadow);
				int SHADOW_WIDTH = dragging.getWidth();
				int SHADOW_HEIGHT = dragging.getHeight();
				g
						.fillRect(mouseX - SHADOW_WIDTH / 2 + offX, mouseY
								- SHADOW_HEIGHT / 2 + offY, SHADOW_WIDTH,
								SHADOW_HEIGHT);
				g.setColor(shadow_outline);
				g
						.drawRect(mouseX - SHADOW_WIDTH / 2 + offX, mouseY
								- SHADOW_HEIGHT / 2 + offY, SHADOW_WIDTH,
								SHADOW_HEIGHT);
			}
		}
		if (selected != null) {
			if (drawingArrow) {
				g.setColor(arrow_color);
			} else
				g.setColor(dragger_color);
			for (float i = 0; i <= 1; i += .5f) {
				for (float j = 0; j <= 1; j += .5f) {
					if (i != .5f || j != .5f) {
						int x = (int) (selected.getX() + i
								* selected.getWidth());
						int y = (int) (selected.getY() + j
								* selected.getHeight());
						g.fillOval(x - DRAGGER_SIZE / 2, y - DRAGGER_SIZE / 2,
								DRAGGER_SIZE, DRAGGER_SIZE);
					}
				}
			}
		}
		if (arrowTo != null) {
			g.setColor(arrow_to_color);
			for (float i = 0; i <= 1; i += .5f) {
				for (float j = 0; j <= 1; j += .5f) {
					if (i != .5f || j != .5f) {
						int x = (int) (arrowTo.getX() + i * arrowTo.getWidth());
						int y = (int) (arrowTo.getY() + j * arrowTo.getHeight());
						g.fillOval(x - DRAGGER_SIZE / 2, y - DRAGGER_SIZE / 2,
								DRAGGER_SIZE, DRAGGER_SIZE);
					}
				}
			}
		}
	}

	private final MouseAdapter adapter = new MouseAdapter() {

		@Override
		public void mouseDragged(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			mouseX /= zoom;
			mouseY /= zoom;
			mouseX += panX;
			mouseY += panY;
			if (dragging != null) {
				dragging.setLocation(mouseX - dragging.getWidth() / 2 + offX,
						mouseY - dragging.getHeight() / 2 + offY);
			} else if (selected != null) {
				if (drawingArrow) {
					arrowTo = controller.getModel().getNodeAt(mouseX, mouseY);
					if (selected == arrowTo)
						arrowTo = null;
				} else {
					if (drag_x != 0) {
						int new_width;
						if (drag_x < 0) {
							new_width = Math.max(1, selected.getX()
									+ selected.getWidth() - mouseX);
							selected.setLocation(selected.getX()
									+ selected.getWidth() - new_width, selected
									.getY());
						} else
							new_width = Math.max(1, mouseX - selected.getX());

						selected.setSize(new_width, selected.getHeight());
						// renderer.dirty(selected);
					}
					if (drag_y != 0) {
						int new_height;
						if (drag_y < 0) {
							new_height = Math.max(1, selected.getY()
									+ selected.getHeight() - mouseY);
							selected.setLocation(selected.getX(), selected
									.getY()
									+ selected.getHeight() - new_height);
						} else
							new_height = Math.max(1, mouseY - selected.getY());

						selected.setSize(selected.getWidth(), new_height);
						// renderer.dirty(selected);
					}
				}
			}
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			mouseX /= zoom;
			mouseY /= zoom;
			mouseX += panX;
			mouseY += panY;
			if (dragging != null) {
				dragging.setLocation(mouseX - dragging.getWidth() / 2 + offX,
						mouseY - dragging.getHeight() / 2 + offY);
				repaint();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			mouseDown = true;
			if (hasFocus() == false)
				requestFocusInWindow();
			int x = e.getX();
			int y = e.getY();
			x /= zoom;
			y /= zoom;
			x += panX;
			y += panY;
			mouseX = x;
			mouseY = y;
			int button = e.getButton();
			if (button == 1) {
				boolean clickedDragger = selected != null
						&& clickedDragger(x, y);
				if (clickedDragger == false)
					selected = controller.getModel().getNodeAt(x, y);
				if (selected != null && !clickedDragger)
					drawingArrow = true;
			} else if (button == 3) {
				if (dragging == null) {
					selected = controller.getModel().getNodeAt(x, y);
					if (selected != null) {
						offX = selected.getX() + selected.getWidth() / 2 - x;
						offY = selected.getY() + selected.getHeight() / 2 - y;
						dragging = selected;
					}
				}
			}
			controller.setSelectedNode(selected);
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mouseDown = false;
			int x = e.getX();
			int y = e.getY();
			x /= zoom;
			y /= zoom;
			x += panX;
			y += panY;
			drag_x = 0;
			drag_y = 0;
			if (dragging != null) {
				dragging.setLocation(x - dragging.getWidth() / 2 + offX, y
						- dragging.getHeight() / 2 + offY);
				controller.getModel().addNode(dragging);
				selected = dragging;
				controller.setSelectedNode(dragging);
				offX = 0;
				offY = 0;
				renderer.dirty(dragging);
				dragging = null;
			}
			if (drawingArrow) {
				if (arrowTo != null) {
					controller.getModel().addLink(selected, arrowTo);
					selected = null;
					arrowTo = null;
				}
				drawingArrow = false;
			}
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			mouseIn = true;
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			mouseIn = false;
			repaint();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (mouseDown)
				return;
			int t = e.getWheelRotation();
			int x = e.getX();
			int y = e.getY();
			x /= zoom;
			y /= zoom;
			x += panX;
			y += panY;
			zoom *= 1 - t / 12.0;
			if (zoom < ZOOM_MIN)
				zoom = ZOOM_MIN;
			if (zoom > ZOOM_MAX)
				zoom = ZOOM_MAX;
			panX = x - e.getX() / zoom;
			panY = y - e.getY() / zoom;
			if (zoom >= 3 && t < 0) {
				SceneNode into = controller.getModel().getNodeAt(x, y);
				if (into != null) {
					zoom = 1;
					panX = 0;
					panY = 0;
					selected = null;
					controller.setSelectedNode(null);
					controller.zoomInto(into);
				}
			} else if (t > 0 && zoom < .4) {
				if (controller.isAtRoot() == false) {
					zoom = 1;
					panX = 0;
					panY = 0;
					selected = null;
					controller.setSelectedNode(null);
					controller.zoomOut();
				}
			}
			repaint();
		}

	};

	private void pressed(int code) {
		if (code == KeyEvent.VK_DELETE) {
			if (selected != null && !mouseDown) {
				controller.getModel().removeNode(selected);
				selected = null;
				repaint();
			}
		}
	}

	boolean[] codes = new boolean[1024];
	private final KeyAdapter keyAdapter = new KeyAdapter() {

		@Override
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();
			if (!codes[code]) {
				codes[code] = true;
				pressed(code);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int code = e.getKeyCode();
			codes[code] = false;
		}

	};

	private final Rectangle r = new Rectangle();

	private boolean clickedDragger(int mx, int my) {
		for (float i = 0; i <= 1; i += .5f) {
			for (float j = 0; j <= 1; j += .5f) {
				if (i != .5f || j != .5f) {
					int x = (int) (selected.getX() + i * selected.getWidth());
					int y = (int) (selected.getY() + j * selected.getHeight());
					r.setBounds(x - DRAGGER_SIZE / 2, y - DRAGGER_SIZE / 2,
							DRAGGER_SIZE, DRAGGER_SIZE);
					if (r.contains(mx, my)) {
						drag_x = (int) (i * 2 - 1);
						drag_y = (int) (j * 2 - 1);
						return true;
					}
				}
			}
		}
		return false;
	}

}
