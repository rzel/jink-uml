package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Provides utility methods for accessing the OS's filesystem.
 */
public class FileSystem {

	public static final int WINDOWS = 0, MAC = 1, LINUX = 2, VISTA = 3,
			UNKNOWN = -1;

	public static final char TOKEN_DELIM = '|';
	public static final String TOKEN_DELIM_STR = "\\|";
	public static final int OS;

	public static final int WINDOWS_BUTTON = 524;

	static {
		String os = System.getProperty("os.name");
		if (os == null)
			OS = UNKNOWN;
		else if (os.contains("vista") || os.contains("Vista"))
			OS = VISTA;
		else if (os.contains("windows") || os.contains("Windows"))
			OS = WINDOWS;
		else if (os.contains("mac") || os.contains("Mac"))
			OS = MAC;
		else if (os.contains("linux") || os.contains("Linux")
				|| os.contains("ntu"))
			OS = LINUX;
		else
			OS = UNKNOWN;
	}

	/**
	 * Gets a reader for a file in the application's data folder. This method
	 * will create a blank file if it does not exist.
	 * 
	 * @param fileName
	 *            The name of the file to open.
	 * @return A reader for the file.
	 */
	public static BufferedReader getReader(String fileName) throws IOException {
		File file = new File(getApplicationDataFolderPath()
				+ File.separatorChar + fileName);
		if (file.exists() == false) {
			file.createNewFile();
		}
		return new BufferedReader(new FileReader(file));
	}

	/**
	 * Gets a stream to a file in the application's data folder.
	 * 
	 * @param fileName
	 *            The name of the file to write to.
	 * @return A writer to a file.
	 */
	public static PrintStream getWriter(String fileName) throws IOException {
		File file = new File(getApplicationDataFolderPath()
				+ File.separatorChar + fileName);
		return new PrintStream(new FileOutputStream(file));
	}

	public static String getApplicationDataFolderPath() {
		String name = System.getProperty("user.home");
		if (name == null) {
			return null;
		} else
			return name;
	}

	public static File addFolder(String name, File parent) {
		String path = parent.getPath();
		if (path.charAt(path.length() - 1) != File.separatorChar)
			path += File.separatorChar;
		File n = new File(path + name);
		if (n.exists() == false)
			n.mkdir();
		return n;
	}

	/**
	 * Makes a java file which represents the child of the parent with the given
	 * name.
	 */
	public static File virtualChild(File parent, String name) {
		String path = parent.getPath();
		if (path.charAt(path.length() - 1) != File.separatorChar)
			path += File.separatorChar;
		File n = new File(path + name);
		return n;
	}

	/**
	 * Gets the folder which this applications stores its data in.
	 * 
	 * @return A reference to the folder.
	 */
	public static File getApplicationDataFolder() {
		String name = System.getProperty("user.home");
		if (name == null) {
			return null;
		} else
			return new File(name);
	}

}
