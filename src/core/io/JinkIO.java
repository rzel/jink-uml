package core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.JList;
import javax.swing.JPanel;

import util.ByteBuffer;
import util.ByteReader;
import core.JavaJinkDocument;
import core.JinkDocument;
import core.PlanningDocument;
import core.model.UMLModel;
import core.model.node.SceneNode;

public class JinkIO {

	private static final short VERSION_ID = 2;

	public static JinkDocument read(File f, JList stackList,
			JPanel optionsHolder) throws IOException {
		ByteReader b = new ByteReader(new FileInputStream(f));
		short ver = b.readShort();
		if (ver != VERSION_ID) {
			throw new RuntimeException("Wrong version. (file = " + ver + ")");
		}
		int type = b.read();
		String title = b.readString();
		short numPlanes = b.readShort();
		LinkedHashMap<Integer, UMLModel> idModels = new LinkedHashMap<Integer, UMLModel>();
		for (int i = 0; i < numPlanes; i++) {
			UMLModel model = UMLModel.readFrom(b);
			if (i == 0) {

			}
			idModels.put(model.getID(), model);
		}
		LinkedHashMap<SceneNode, UMLModel> planes = new LinkedHashMap<SceneNode, UMLModel>();
		for (int i = 0; i < numPlanes; i++) {
			int keyID = b.readShort();
			int modelID = b.readShort();
			SceneNode sn = findSceneNode(keyID, idModels.values());
			UMLModel model = findModel(modelID, idModels.values());
			if (sn == null)
				throw new RuntimeException("Could not find key with id: "
						+ keyID);
			if (model == null)
				throw new RuntimeException("Could not find model with id: "
						+ modelID);
			planes.put(sn, model);
		}
		if (type == 0)
			return new PlanningDocument(title, stackList, optionsHolder, planes);
		else if (type == 1)
			return new JavaJinkDocument(title, stackList, optionsHolder, planes);
		else
			throw new RuntimeException("Unknown Doc Type: " + type);
	}

	private static SceneNode findSceneNode(int id, Iterable<UMLModel> m) {
		if (id == 0) {
			return SceneNode.ROOT_NODE;
		}
		for (UMLModel model : m) {
			SceneNode sn = model.nodeForID(id);
			if (sn != null)
				return sn;
		}
		return null;
	}

	private static UMLModel findModel(int id, Iterable<UMLModel> m) {
		for (UMLModel model : m) {
			if (model.getID() == id)
				return model;
		}
		return null;
	}

	public static void write(JinkDocument jd, File to) throws IOException {
		ByteBuffer b = new ByteBuffer();
		// first plane to write out is the root
		HashMap<SceneNode, UMLModel> planes = jd.getPlaneShifts();
		b.addShort(VERSION_ID); // version
		b.add(jd.getTypeID());
		b.addString(jd.getTitle());
		b.addShort((short) planes.size()); // number of planes
		for (SceneNode sn : planes.keySet()) {
			UMLModel m = planes.get(sn);
			m.writeTo(b);
		}
		for (SceneNode sn : planes.keySet()) {
			b.addShort((short) sn.getID());
			b.addShort((short) planes.get(sn).getID());
		}
		FileOutputStream fos = new FileOutputStream(to);
		fos.write(b.getBuffer(), 0, b.size());
		fos.close();
	}

}
