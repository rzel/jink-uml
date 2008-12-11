package util;

import java.io.IOException;
import java.io.InputStream;

public class ByteReader {

	private final byte[] b;
	private int c = 0;

	public ByteReader(byte[] data) {
		this.b = data;
	}

	public ByteReader(InputStream is) throws IOException {
		ByteBuffer bb = new ByteBuffer();
		byte[] buf = new byte[1024];
		while (true) {
			int len = is.read(buf);
			if (len <= 0)
				break;
			bb.add(buf, 0, len);
		}
		b = bb.toByteArray();
	}

	public boolean isDone() {
		return c >= b.length;
	}

	public byte read() {
		return b[c++];
	}

	public short readShort() {
		return (short) (((b[c++]) << 8) + (b[c++] & 0xFF));
	}

	public int readInt() {
		return (b[c++] << 24) + ((b[c++] & 0xFF) << 16)
				+ ((b[c++] & 0xFF) << 8) + (b[c++] & 0xFF);
	}

	public int peekInt() {
		int ret = readInt();
		c -= 4;
		return ret;
	}

	public float readFloat() {
		int i = readInt();
		return Float.intBitsToFloat(i);
	}

	public long readLong() {
		return ((((long) b[c++]) & 0xFF) + ((((long) b[c++]) & 0xFF) << 8)
				+ ((((long) b[c++]) & 0xFF) << 16)
				+ ((((long) b[c++]) & 0xFF) << 24)
				+ ((((long) b[c++]) & 0xFF) << 32)
				+ ((((long) b[c++]) & 0xFF) << 40)
				+ ((((long) b[c++]) & 0xFF) << 48) + ((((long) b[c++]) & 0xFF) << 56));
	}

	public double readDouble() {
		long t = readLong();
		return Double.longBitsToDouble(t);
	}

	public String readString() {
		int len = readShort();
		String ret = new String(b, c, len);
		c += len;
		return ret;
	}

	public boolean readBoolean() {
		byte b = read();
		return b == 1;
	}

}
