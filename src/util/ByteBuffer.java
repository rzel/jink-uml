package util;

import java.io.IOException;
import java.io.InputStream;

public class ByteBuffer {

	byte[] data;
	int size = 0;

	public ByteBuffer() {
		this(16);
	}

	public ByteBuffer(int capacity) {
		data = new byte[capacity];
	}

	public byte[] toByteArray() {
		byte[] copy = new byte[size];
		for (int i = 0; i < size; i++) {
			copy[i] = data[i];
		}
		return copy;
	}

	public byte[] getBuffer() {
		return data;
	}

	public void add(ByteBuffer buffer) {
		add(buffer.data, 0, buffer.size);
	}

	public void add(byte[] bytes) {
		add(bytes, 0, bytes.length);
	}

	public void add(byte[] bytes, int offset, int len) {
		for (int i = offset; i < offset + len; i++) {
			add(bytes[i]);
		}
	}

	public void add(int b) {
		if (size == data.length)
			resize();
		data[size++] = (byte) b;
	}

	public void addShort(short b) {
		add((byte) (b >>> 8));
		add((byte) (b));
	}

	public void addInt(int b) {
		add((byte) (b >>> 24));
		add((byte) (b >>> 16));
		add((byte) (b >>> 8));
		add((byte) (b));
	}

	public void addFloat(float f) {
		int i = Float.floatToRawIntBits(f);
		addInt(i);
	}

	public void addDouble(double d) {
		long t = Double.doubleToRawLongBits(d);
		addLong(t);
	}

	public void addLong(long b) {
		add((byte) b);
		b >>>= 8;
		add((byte) b);
		b >>>= 8;
		add((byte) b);
		b >>>= 8;
		add((byte) b);
		b >>>= 8;
		add((byte) b);
		b >>>= 8;
		add((byte) b);
		b >>>= 8;
		add((byte) b);
		b >>>= 8;
		add((byte) b);
		b >>>= 8;
	}

	public void addString(String s) {
		addShort((short) s.length());
		add(s.getBytes());
	}

	private void resize() {
		byte[] copy = new byte[data.length * 2];
		for (int i = 0; i < data.length; i++) {
			copy[i] = data[i];
		}
		data = copy;
	}

	public int size() {
		return size;
	}

	public void reset() {
		size = 0;
	}

	private byte[] inputBuf;

	/**
	 * Reads this input stream into the byte buffer until it runs out of bytes.
	 */
	public void read(InputStream is) throws IOException {
		if (inputBuf == null)
			inputBuf = new byte[1024];
		int len = 1;
		while (len > 0) {
			len = is.read(inputBuf);
			add(inputBuf, 0, len);
		}
	}

	public void addBoolean(boolean b) {
		if (b)
			add(1);
		else
			add(0);
	}

}
