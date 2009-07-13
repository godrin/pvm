package vm_java.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.LineInputStream;

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
	
	
	public void testKlass() throws VMExceptionOutOfMemory, BlockIsFinalException, VMException, ParseError, IOException {
		runExample(new File(getExamplePath()+"/if.pvm"));
	}

	public void donttestExamples() throws VMExceptionOutOfMemory,
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
		
		String wantedOutput=getWantedOutput(f);

		ASTProgram astP = lp.parseFile(f);

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

	private String getWantedOutput(File f) throws IOException {
		LineInputStream lis=new LineInputStream(new FileInputStream(f));
		String line;
		while((line=lis.readLine())!=null) {
			System.out.println(line);
		}
		
		return null;
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
