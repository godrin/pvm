package vm_java.context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * maybe add some other (virtual) paths, that provide programs like web-sources
 * or ruby-parsers
 * 
 * @author davidkamphausen
 * 
 */
public class SourcePath {
	private String path;

	public SourcePath(String p) {
		path = p;
	}

	public String getContent(String filename) throws IOException {
		if (filename.matches("[a-zA-Z0-9_.]*")) {
			File f = new File(path + File.separator + filename);
			if (f.isFile()) {
				StringBuilder b = new StringBuilder();

				FileInputStream fis = new FileInputStream(f);
				byte[] buffer = new byte[1024];
				int read;
				while ((read = fis.read(buffer)) > 0) {
					b.append(new String(buffer, 0, read));
				}

				return b.toString();
			}
		}
		return null;
	}
}
