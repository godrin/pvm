package vm_java.pruby;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import vm_java.internal.VMLog;
import vm_java.internal.VMLog.Level;

public class PRubyParserMRIRuby implements PRubyParser {

	@Override
	public String parse(PRubySourceDef source) {
		String[] array = new String[] { "/usr/bin/ruby",
				PRubyParserJRuby.PARSER_RB

		};

		System.out.println("Parsing:");

		String input = source.getString();
		System.out.println(input);
		System.out.println("Parsed.");

		String ret = runPipe(array, input);
		return ret;
	}

	public String runPipe(String[] array, String input) {
		Runtime r = Runtime.getRuntime();

		if (input == null)
			return null;

		try {
			Process proc = r.exec(array);

			// write
			OutputStream os = proc.getOutputStream();
			InputStream error = proc.getErrorStream();

			os.write(input.getBytes());
			os.close();

			// proc.getOutputStream();
			int read;
			byte[] buffer = new byte[1024];

			while ((read = error.read(buffer)) > 0) {
				System.out.println(new String(buffer, 0, read));
			}

			proc.waitFor();

			// read
			StringBuilder b = new StringBuilder();
			while ((read = proc.getInputStream().read(buffer)) > 0) {
				b.append(new String(buffer, 0, read));
			}

			return b.toString();
		} catch (IOException e) {
			VMLog.error(e);
		} catch (InterruptedException e) {
			VMLog.error(e);
		}

		return null;
	}

	public static void main(String[] args) {
		VMLog.setLogLevels(new Level[] { VMLog.Level.DEBUG, Level.ERROR,
				Level.WARN });

		PRubyParserMRIRuby p = new PRubyParserMRIRuby();
		PRubySourceDef def = new PRubySourceDef("simple_function.prb",
				new SinglePathSourceSource(new File("ruby")));
		String res = p.parse(def);
		System.out.println(res);
	}

}
