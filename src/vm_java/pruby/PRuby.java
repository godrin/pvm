package vm_java.pruby;

import java.io.IOException;

import vm_java.VM;
import vm_java.code.BlockIsFinalException;
import vm_java.code.Program;
import vm_java.code.VMException;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
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

	public void run(PRubySourceDef source) throws ParseError, IOException,
			VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		
		String vmSource = parser.parse(source);
		VMLog.debug("Source:");
		VMLog.debug(vmSource);
		ASTProgram astP = vm.getParser().parseData(vmSource, source.getPath());
		Program prg = astP.instantiate(vmc);
		VMScope scope = vmc.createScope();
		vm.addJob(prg.execution(scope));
		VMLog.debug("CODE::::");
		VMLog.debug(prg.toCode());
		VMLog.debug("CODE!");
		vm.run();
	}
	

}
