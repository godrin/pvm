package vm_java.pruby;

public class PRubySourceDef {
	private String path;
	private SourceSource source;

	public PRubySourceDef(String pPath, SourceSource pSource) {
		path = pPath;
		source = pSource;
	}

	/**
	 * @return the path
	 */
	public synchronized final String getPath() {
		return path;
	}

	/**
	 * @return the source
	 */
	public synchronized final SourceSource getSource() {
		return source;
	}
	
	public byte[] getData() {
		return source.getProgram(path);
	}
	
	public String getString() {
		return source.getProgramAsString(path);
	}
}
