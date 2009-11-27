package vm_java.pruby;

import java.io.File;
import java.io.IOException;

import vm_java.internal.VMLog;
import vm_java.tools.FileTool;

public class SinglePathSourceSource implements SourceSource {

	File dir;

	public SinglePathSourceSource(File pdir) {
		dir = pdir;
	}

	public byte[] getProgram(String path) {
		if (path.matches("^[a-zA-Z0-9_\\.]*$")) {
			File f = new File(dir.getAbsolutePath() + File.separator + path);
			if (f.isFile()) {
				try {
					return FileTool.loadFile(f.getAbsoluteFile());
				} catch (IOException e) {
					VMLog.error(e);
				}
			}
		}
		return null;
	}

	public String getProgramAsString(String path) {
		if (path.matches("^[a-zA-Z0-9_\\.]*$")) {
			File f = new File(dir.getAbsolutePath() + File.separator + path);
			System.out.println("("+f+")");
			if (f.isFile()) {
				try {
					return FileTool.loadFileString(f.getAbsoluteFile());
				} catch (IOException e) {
					VMLog.error(e);
				}
			} else {
				VMLog.error("File "+f+" not found");
			}
		}
		return null;
	}

}
