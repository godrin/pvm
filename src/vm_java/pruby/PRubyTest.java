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

//		test("src/vm_java/examples", "example.prb");
		//test("ruby", "simple_function.prb");
		test("ruby/semantic/checks", "defn.rb");

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
		pruby.run(new PRubySourceDef(file, source));
		vm.join();
		VMIO.clear();
	}
}
