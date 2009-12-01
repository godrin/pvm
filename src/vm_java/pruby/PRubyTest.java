package vm_java.pruby;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import vm_java.VM;
import vm_java.code.BlockIsFinalException;
import vm_java.code.VMException;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.internal.VMLog;
import vm_java.internal.VMLog.Level;
import vm_java.llparser.LLParser2;
import vm_java.llparser.LLParser2.ParseError;
import vm_java.types.buildins.VMIO;

public class PRubyTest extends TestCase {
	public void setUp() {

		VMLog.setLogLevels(new Level[] { VMLog.Level.DEBUG, Level.ERROR,
				Level.WARN });
	}

	public void testPRuby() throws ParseError, IOException,
			VMExceptionOutOfMemory, BlockIsFinalException, VMException,
			InterruptedException {
		String dir = "ruby/semantic/checks";

		if (true) {
			String file = "exception_simple.rb";
			test(dir, file);
		} else {

			for (String file : new File(dir).list()) {
				if (file.matches(".*\\.rb")) {
					test(dir, file);
				}
			}
		}

	}

	public void testPRubyCompile() throws ParseError, IOException,
			VMExceptionOutOfMemory, BlockIsFinalException, VMException,
			InterruptedException {

		test("/usr/lib/ruby/1.8/racc/", "parser.rb");
	}

	private void test(String path, String file) throws ParseError, IOException,
			VMExceptionOutOfMemory, BlockIsFinalException, VMException,
			InterruptedException {
		String p = path;
		SinglePathSourceSource source = new SinglePathSourceSource(new File(p));
		LLParser2 vmParser = new LLParser2();
		VM vm = new VM(vmParser);
		PRubyParser rubyParser = new PRubyParserMRIRuby();
		PRuby pruby = new PRuby(vm, rubyParser);
		pruby.run(new PRubySourceDef(file, source), testAuthorizations());
		vm.join();
		String wan = getWantedOutput(source, file);
		System.out.println("Content:" + VMIO.content());
		assertEquals(wan, VMIO.content());
		VMIO.clear();

	}

	private Authorizations testAuthorizations() {
		return Authorizations.all();
	}

	private String getWantedOutput(SinglePathSourceSource source, String file) {
		boolean started = false;
		StringBuilder b = new StringBuilder();
		for (String line : source.getProgramAsString(file).split("\n")) {
			if (line.equals("# OUTPUT")) {
				started = true;
			} else if (started) {
				b.append(line.replaceAll("^# ", "")).append("\n");
			}
		}
		return b.toString();
	}
}
