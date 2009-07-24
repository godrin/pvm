package vm_java.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class FileTool {
	public static byte[] loadFile(File path) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream fis = new FileInputStream(path);
		byte[] buffer = new byte[1024];
		int read;

		while ((read = fis.read(buffer)) > 0) {
			baos.write(buffer, 0, read);
		}

		return baos.toByteArray();

	}

	public static String loadFileString(File path) throws IOException {
		StringBuilder b=new StringBuilder();
		FileInputStream fis = new FileInputStream(path);
		byte[] buffer = new byte[1024];
		int read;

		while ((read = fis.read(buffer)) > 0) {
			b.append(new String(buffer, 0, read));
		}

		return b.toString();
	}
}
