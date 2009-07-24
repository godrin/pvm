package vm_java.pruby;

public interface SourceSource {
	byte[] getProgram(String path);

	String getProgramAsString(String path);
}
