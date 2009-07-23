package vm_java.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import vm_java.VM;
import vm_java.code.BlockIsFinalException;
import vm_java.code.Program;
import vm_java.code.VMException;
import vm_java.context.SourcePath;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.internal.VMLog.Level;
import vm_java.llparser.LLParser2;
import vm_java.llparser.LLParser2.ParseError;
import vm_java.llparser.ast.ASTProgram;
import vm_java.types.VMIO;

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.LineInputStream;

public class ExampleTests extends TestCase {
	public void setUp() {

		VMLog.setLogLevels(new Level[] { VMLog.Level.DEBUG, Level.ERROR,
				Level.WARN });
	}

	public void testSingle() throws VMExceptionOutOfMemory,
			BlockIsFinalException, VMException, ParseError, IOException,
			TestFailedException {
		runExample(new File(getExamplePath() + "/exception.pvm"));
	}

	public void testExamples() throws VMExceptionOutOfMemory,
			BlockIsFinalException, VMException, ParseError, IOException,
			TestFailedException {

		for (File example : getExamples()) {
			runExample(example);
		}

	}

	private void runExample(File f) throws VMExceptionOutOfMemory,
			BlockIsFinalException, VMException, ParseError, IOException,
			TestFailedException {
		LLParser2 lp = new LLParser2();
		VM vm = new VM(lp);
		VMContext vmc = vm.createContext();

		String curDir = System.getProperty("user.dir");
		VMLog.debug(curDir);

		String wantedOutput = getWantedOutput(f);

		ASTProgram astP = lp.parseFile(f);

		vmc.addPath(new SourcePath(f.getParent()));

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
		String buf = VMIO.content();
		if (!buf.equals(wantedOutput)) {
			System.out.println("Test:"+f);
			System.out.println("WANTED:" + wantedOutput.replace("\n", ":\n") + ":");
			System.out.println("GOT:" + buf.replace("\n", ":\n") + ":");
			throw new TestFailedException();
		}
		VMIO.clear();
		
		if(vm.getFatalError()!=null) {
			assertNull(vm.getFatalError());
		}
	}

	private String getWantedOutput(File f) throws IOException {
		LineInputStream lis = new LineInputStream(new FileInputStream(f));
		String line;
		boolean outputFollowing = false;
		StringBuilder b = new StringBuilder();

		while ((line = lis.readLine()) != null) {
			if (outputFollowing) {
				b.append(line.replaceFirst("^. *", "") + "\n");
			} else if (line.matches(".*#.*OUTPUT.*")) {
				outputFollowing = true;
			}
		}

		return b.toString();
	}

	private List<File> getExamples() {
		List<File> list = new ArrayList<File>();
		for (File f : new File(getExamplePath()).listFiles()) {
			if (f.toString().matches(".*\\.pvm")) {
				list.add(f);
			}
		}
		return list;
	}

	private String getExamplePath() {
		return "/Users/davidkamphausen/Documents/workspaceJava/vm_java/src/vm_java/examples";
	}
}
