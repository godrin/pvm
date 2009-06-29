/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
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

	protected void run(VMScope pScope) throws VMException, VMExceptionOutOfMemory,
			VMExceptionFunctionNotFound {
	}

	public CodeBlock getBlock() {
		return mBlock;
	}

	@Override
	public void enqueue(VMScope scope) {
		getVM().enqueue(new Task(this,scope));

	}

	@Override
	public
	void go(VMScope scope) throws VMException, VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		// TODO Auto-generated method stub
		mBlock.execute(scope);

	}
}
