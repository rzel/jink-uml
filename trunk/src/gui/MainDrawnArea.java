package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JComponent;

import util.ByteBuffer;
import util.ByteReader;
import util.JinkUtils;
import core.JinkDocument;
import core.Perspective;
import core.model.node.SceneNode;

public class MainDrawnArea extends JComponent {

	private static final Color shadow = new Color(0, 0, 0, 50),
			dragger_color = new Color(0, 0, 0, 100), arrow_color = new Color(0,
					255, 0, 100), shadow_outline = Color.gray,
			arrow_to_color = new Color(255, 100, 0, 100);
	private static final int DRAGGER_SIZE = 9;
	private static final double ZOOM_MIN = .01, ZOOM_MAX = 20;
	private static final int GRID_SIZE = 20;

	private final JinkDocument controller;
	private final ModelRenderer renderer;
	private int offX, offY;
	private int mouseX, mouseY;
	private boolean mouseIn = false;
	private boolean mouseDown = false;
	private boolean drawingArrow = false;
	private boolean snapToGrid = true;
	public SceneNode dragging, selected, arrowTo;
	private int drag_x, drag_y;
	private double zoom = 1;
	private double panX = 0;
	private double panY = 0;

	public MainDrawnArea(JinkDocument controller, ModelRenderer renderer) {
		this.controller = controller;
		this.renderer = renderer;
		this.addMouseListener(adapter);
		this.addMouseMotionListener(adapter);
		this.addMouseWheelListener(adapter);
		this.addKeyListener(keyAdapter);
		startScrollThread();
	}

	@Override
	protected void paintComponent(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		int w = getWidth(), h = getHeight();
		g.setColor(Color.white);
		g.fillRect(0, 0, w, h);
		renderer.render(g, zoom, panX, panY, w, h);
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
			if (snapToGrid) {
				mouseX = mouseX / GRID_SIZE * GRID_SIZE;
				mouseY = mouseY / GRID_SIZE * GRID_SIZE;
			}
			if (dragging != null) {
				dragging.setLocation(mouseX - dragging.getWidth() / 2 + offX,
						mouseY - dragging.getHeight() / 2 + offY);
			} else if (selected != null) {
				if (drawingArrow) {
					arrowTo = controller.getModel().getNodeAt(mouseX, mouseY);
					if (selected == arrowTo)
						arrowTo = null;
				} else {
					mouseX += GRID_SIZE / 2;
					mouseY += GRID_SIZE / 2;
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
			if (snapToGrid) {
				mouseX = mouseX / GRID_SIZE * GRID_SIZE;
				mouseY = mouseY / GRID_SIZE * GRID_SIZE;
			}
			if (dragging != null) {
				dragging.setLocation(mouseX - dragging.getWidth() / 2 + offX,
						mouseY - dragging.getHeight() / 2 + offY);
				repaint();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			controller.setDirty(true);
			mouseDown = true;
			if (hasFocus() == false)
				requestFocusInWindow();
			int x = e.getX();
			int y = e.getY();
			x /= zoom;
			y /= zoom;
			x += panX;
			y += panY;
			boolean clickedDragger = selected != null && clickedDragger(x, y);
			if (snapToGrid) {
				x = x / GRID_SIZE * GRID_SIZE;
				y = y / GRID_SIZE * GRID_SIZE;
			}
			mouseX = x;
			mouseY = y;
			int button = e.getButton();
			if (button == 1) {
				if (e.getClickCount() == 2) {
					selected = controller.getModel().getNodeAt(x, y);
					controller.activate(selected);
				} else {
					if (clickedDragger == false)
						selected = controller.getModel().getNodeAt(x, y);
					if (selected != null && !clickedDragger)
						drawingArrow = true;
				}
			} else if (button == 2) {
				selected = controller.getModel().getNodeAt(x, y);
				if (selected != null)
					controller.getModel().removeLinksFrom(selected);
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
			if (snapToGrid) {
				x = x / GRID_SIZE * GRID_SIZE;
				y = y / GRID_SIZE * GRID_SIZE;
			}
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
			requestFocusInWindow();
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
			double t = e.getWheelRotation();
			zoom(t);
		}
	};

	private void copy() {
		if (selected != null) {
			ByteBuffer bb = new ByteBuffer();
			selected.writeTo(bb);
			JinkUtils.setClipboard(bb.toByteArray());
		} else {
			JinkUtils.setClipboard(null);
		}
	}

	private void paste() {
		byte[] bytes = JinkUtils.getClipboard();
		if (bytes == null)
			return;
		ByteReader b = new ByteReader(bytes);
		SceneNode s = SceneNode.readFrom(b);
		s.setNextID();
		s.setLocation(mouseX - s.getWidth() / 2, mouseY - s.getHeight() / 2);
		controller.getModel().addNode(s);
		repaint();
	}

	private void zoom(double t) {
		int ox = getWidth() / 2;
		int oy = getHeight() / 2;
		int x = ox;
		int y = oy;
		x /= zoom;
		y /= zoom;
		x += panX;
		y += panY;
		zoom *= 1 - t / 12.0;
		if (zoom < ZOOM_MIN)
			zoom = ZOOM_MIN;
		if (zoom > ZOOM_MAX)
			zoom = ZOOM_MAX;
		panX = x - ox / zoom;
		panY = y - oy / zoom;
		panX = panX / GRID_SIZE * GRID_SIZE;
		panY = panY / GRID_SIZE * GRID_SIZE;
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

	private void pressed(int code) {
		if (code == KeyEvent.VK_DELETE) {
			if (selected != null && !mouseDown) {
				controller.getModel().removeNode(selected);
				selected = null;
				repaint();
			}
		}
		if (codes[KeyEvent.VK_CONTROL]) {
			if (code == KeyEvent.VK_C) {
				copy();
			} else if (code == KeyEvent.VK_V) {
				paste();
			}
		}
	}

	private boolean noMods() {
		return !codes[KeyEvent.VK_CONTROL] && !codes[KeyEvent.VK_SHIFT];
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

	private void startScrollThread() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (noMods()) {
						if (codes[KeyEvent.VK_LEFT] || codes[KeyEvent.VK_A]) {
							panX -= GRID_SIZE;
							repaint();
						} else if (codes[KeyEvent.VK_RIGHT]
								|| codes[KeyEvent.VK_D]) {
							panX += GRID_SIZE;
							repaint();
						}
						if (codes[KeyEvent.VK_UP] || codes[KeyEvent.VK_W]) {
							panY -= GRID_SIZE;
							repaint();
						} else if (codes[KeyEvent.VK_DOWN]
								|| codes[KeyEvent.VK_S]) {
							panY += GRID_SIZE;
							repaint();
						}
						if (codes[KeyEvent.VK_PAGE_UP]) {
							zoom(-1);
						} else if (codes[KeyEvent.VK_PAGE_DOWN]) {
							zoom(1);
						}
					}
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
					}
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}

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

	public void setAlignGrid(boolean b) {
		snapToGrid = b;
	}

	public void clearSelection() {
		selected = null;
		repaint();
	}

	public void centerOn(Rectangle bounds) {
		panX = bounds.x - GRID_SIZE;
		panY = bounds.y - GRID_SIZE;
		repaint();
	}

	public Perspective getPerspective() {
		return new Perspective(panX, panY, zoom);
	}

	public void applyPerspective(Perspective perspective) {
		if (perspective == null) {
			centerOn(controller.getModel().getBounds());
			return;
		}
		panX = perspective.panX;
		panY = perspective.panY;
		zoom = perspective.zoom;
		repaint();
	}
}
