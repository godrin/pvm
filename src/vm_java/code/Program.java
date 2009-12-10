/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.RunTask;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;

/**
 * 
 * @author davidkamphausen
 */
public class Program extends BasicObject implements Runnable {

	private CodeBlock mBlock;

	public Program(VMContext pContext, CodeBlock pBlock)
			throws VMExceptionOutOfMemory {
		super(pContext);
		mBlock = pBlock;
	}

	protected void run(VMScope pScope) throws VMInternalException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
	}

	public CodeBlock getBlock() {
		return mBlock;
	}

	public void go(VMScope scope,Task pParentTask) throws VMInternalException, VMExceptionOutOfMemory,
			VMExceptionFunctionNotFound {
		// TODO Auto-generated method stub
		getVM().enqueue(mBlock.execution(scope, pParentTask));
	}

	public Task execution(VMScope scope) {
		return new RunTask(scope, this);
	}

	public Task execution(VMScope scope, Task pParent) {
		return new RunTask(scope, pParent, this);
	}

	@Override
	public String inspect() {
		return "[Program]";
	}

	public Code toCode() {
		return mBlock.toCode();
	}

	@Override
	public String inlineCode() {
		return toCode().toString();
	}
}
