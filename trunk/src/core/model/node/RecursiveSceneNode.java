package core.model.node;

import java.awt.Dimension;

/**
 * Contains a whole new scene inside of this scene.
 * 
 * @author Jason
 * 
 */
public class RecursiveSceneNode extends SceneNode {

	@Override
	public Dimension getInitalSize() {
		return new Dimension(100, 100);
	}

	@Override
	public String getNodeType() {
		return "Node";
	}

}
