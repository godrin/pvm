package vm_java.code;

import java.util.ArrayList;
import java.util.List;

public class Code {
	private List<String> lines = new ArrayList<String>();

	public void add(String line) {
		lines.add(line);
	}

	public void add(Code code) {
		if (code != null)
			lines.addAll(code.lines);
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		for (String l : lines) {
			b.append(l).append("\n");
		}
		return b.toString();
	}

	public Code indent() {
		Code c = new Code();
		for (String l : lines) {
			c.add("  " + l);
		}
		return c;
	}

	public void addToLastLine(String string) {
		lines.set(lines.size() - 1, lines.get(lines.size() - 1) + string);
	}
}
