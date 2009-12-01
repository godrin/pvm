package vm_java.pruby;

import java.util.Map;
import java.util.TreeMap;

public class Arguments {

	Map<String, String> ass = new TreeMap<String, String>();
	String script;
	boolean interactive;

	public Arguments(String argv[]) {
		interactive = false;

		check(argv);
	}
	
	public String getScript() {
		return script;
	}
	
	public boolean isInteractive() {
		return interactive;
	}

	private void check(String[] argv) {
		for (String arg : argv) {
			if (arg.matches("^--.*=.*$")) {
				optionAssign(arg);
			}
			else if (arg.matches("^--.*$")) {
				optionSingle(arg);
			} else {
				script=arg;
			}
		}
	}

	private void optionSingle(String arg) {
		if("--interactive".equals(arg)) {
			interactive=true;
		}
	}

	private void optionAssign(String arg) {
		String[] b = arg.substring(2).split("=");
		ass.put(b[0], b[1]);
	}
}
