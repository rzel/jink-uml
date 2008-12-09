package util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class JinkFileFilter extends FileFilter {

	@Override
	public boolean accept(File pathname) {
		if (pathname.isDirectory())
			return true;
		else {
			String name = pathname.getName();
			if (name.endsWith(".jink")) {
				return true;
			} else
				return false;
		}
	}

	@Override
	public String getDescription() {
		return "Jink UML Files (.jink)";
	}

}
