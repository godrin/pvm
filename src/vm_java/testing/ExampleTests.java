package vm_java.testing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import vm_java.VM;
import vm_java.code.BlockIsFinalException;
import vm_java.code.Program;
import vm_java.code.VMException;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.internal.VMLog.Level;
import vm_java.llparser.LLParser2;
import vm_java.llparser.LLParser2.ParseError;
import vm_java.llparser.ast.ASTProgram;

public class ExampleTests extends TestCase {
	public void setUp() {

		VMLog.setLogLevels(new Level[] { VMLog.Level.DEBUG, Level.ERROR,
				Level.WARN });
	}

	public void testExamples() throws VMExceptionOutOfMemory,
			BlockIsFinalException, VMException, ParseError, IOException {

		for (File example : getExamples()) {
			runExample(example);
		}

	}

	private void runExample(File f) throws VMExceptionOutOfMemory,
			BlockIsFinalException, VMException, ParseError, IOException {
		VM vm = new VM();
		VMContext vmc = vm.createContext();

		LLParser2 lp = new LLParser2();
		String curDir = System.getProperty("user.dir");
		VMLog.debug(curDir);

		ASTProgram astP = lp.parseFile(f);

		// lp.parse(content);
		Program prg = astP.instantiate(vmc);
		VMScope scope = vmc.createScope();
		vm.addJob(prg.execution(scope));
		vm.run();
		try {
			vm.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<File> getExamples() {
		List<File> list = new ArrayList<File>();
		for (File f : getExamplePath().listFiles()) {
			if (f.toString().matches(".*\\.pvm")) {
				list.add(f);
			}
		}
		return list;
	}

	private File getExamplePath() {
		return new File(
				"/Users/davidkamphausen/Documents/workspaceJava/vm_java/src/vm_java/examples");
	}
}
