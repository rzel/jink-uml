package util;

import java.awt.Rectangle;
import java.util.Iterator;

import core.model.node.SceneNode;

public class JinkUtils {

	public static Rectangle calculateBounds(Iterator<SceneNode> iter) {
		if (iter.hasNext() == false)
			return new Rectangle(0, 0, 1, 1);
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		while (iter.hasNext()) {
			SceneNode n = iter.next();
			Rectangle b = n.getBounds();
			minX = Math.min(minX, b.x);
			maxX = Math.max(maxX, b.x + b.width);
			minY = Math.min(minY, b.y);
			maxY = Math.max(maxY, b.y + b.height);
		}
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	public static byte[] getClipboard() {
		return clip;
	}

	private static byte[] clip = null;

	public static void setClipboard(byte[] b) {
		clip = b;
	}

}
