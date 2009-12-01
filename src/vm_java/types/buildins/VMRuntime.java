/**
 * 
 */
package vm_java.types.buildins;

import java.io.IOException;

import vm_java.VM;
import vm_java.code.BlockIsFinalException;
import vm_java.code.Code;
import vm_java.code.Program;
import vm_java.code.VMException;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.llparser.LLParser2.ParseError;
import vm_java.llparser.ast.ASTProgram;
import vm_java.machine.Task;
import vm_java.types.basic.VMBuildinObjectBase;
import vm_java.types.foundation.VMString;

/**
 * @author davidkamphausen
 * 
 */
public class VMRuntime extends VMBuildinObjectBase {

	/**
	 * @param context
	 * @throws VMExceptionOutOfMemory
	 */
	public VMRuntime(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}

	public static VMScope require(Task task, VMString filename)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException,
			ParseError, IOException {
		// FIXME: check if already loaded

		return load(task, filename);
	}

	public static synchronized VMScope puts(VMScope scope, VMString s)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException,
			ParseError, IOException {
		System.out.println(s);
		return scope;
	}

	public static VMScope load(Task task, VMString filename)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException,
			ParseError, IOException {

		VMScope scope = task.getScope();
		VMContext context = scope.getContext();
		VM vm = context.getVM();

		String source = context.loadSource(filename.getContent());
		ASTProgram astP = vm.getParser().parseData(source,
				filename.getContent());

		Program prg = astP.instantiate(context);
		vm.addJob(prg.execution(scope, task));
		return scope;
	}

	@Override
	public Code toCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String inlineCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String inspect() {
		// TODO Auto-generated method stub
		return null;
	}

}
