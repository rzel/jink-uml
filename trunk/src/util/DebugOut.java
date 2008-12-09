package util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class DebugOut extends PrintStream {

	public DebugOut() {
		super(new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				System.err.println("here");
				new Exception().printStackTrace();
			}

		});
	}

}
