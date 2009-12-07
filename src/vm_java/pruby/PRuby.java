package vm_java.pruby;

import java.io.File;
import java.io.IOException;

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

public class PRuby {
	VM vm;
	PRubyParser parser;
	VMContext vmc;

	public PRuby(VM pVM, PRubyParser pParser) {
		vm = pVM;
		parser = pParser;
		vmc = vm.createContext();
	}

	public void run(PRubySourceDef source, Authorizations authorizations)
			throws ParseError, IOException, VMExceptionOutOfMemory,
			BlockIsFinalException, VMException {

		String vmSource = parser.parse(source);
		VMLog.debug("Source:");
		VMLog.debug(vmSource);
		ASTProgram astP = vm.getParser().parseData(vmSource, source.getPath());
		Program prg = astP.instantiate(vmc);
		VMScope scope = vmc.createScope(authorizations);
		vm.addJob(prg.execution(scope));
		VMLog.debug("CODE::::");
		VMLog.debug(prg.toCode());
		VMLog.debug("CODE!");
		vm.run();
	}

	public static void main(String[] args) throws ParseError, IOException,
			VMExceptionOutOfMemory, BlockIsFinalException, VMException,
			InterruptedException {

		Arguments arg = new Arguments(args);
		// FIXME: parse args correctly
		runFile(arg);

	}

	private static void runFile(Arguments arg) throws ParseError, IOException,
			VMExceptionOutOfMemory, BlockIsFinalException, VMException,
			InterruptedException {
		File f = new File(arg.getScript());
		File dir = f.getParentFile();
		String file = f.getName();

		SinglePathSourceSource source = new SinglePathSourceSource(dir);
		LLParser2 vmParser = new LLParser2();
		VM vm = new VM(vmParser);
		PRubyParser rubyParser = new PRubyParserMRIRuby();
		PRuby pruby = new PRuby(vm, rubyParser);
		pruby.run(new PRubySourceDef(file, source), getAuthorizations(arg));
		vm.join();
	}

	private static Authorizations getAuthorizations(Arguments arg) {
		Authorizations l = new Authorizations();
		if (arg.isInteractive()) {
			l.add(new Authorization("VMIO"));
		}
		l.commit();
		if (arg.isDebuggin()) {
			VMLog.setLogLevels(new Level[] { Level.DEBUG, Level.ERROR,
					Level.WARN });
		}
		return l;
	}

}
