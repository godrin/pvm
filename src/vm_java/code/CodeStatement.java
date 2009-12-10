/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;

/**
 * 
 * @author davidkamphausen
 */
public abstract class CodeStatement extends SourceBased {

	CodeStatement(VMContext pContext, SourceInfo source)
			throws VMExceptionOutOfMemory {
		super(pContext, source);
	}

	protected void assertNotNull(ObjectName leftMember) throws VMInternalException {
		if (leftMember == null)
			throw new VMInternalException(this, "Var is null!");
	}

	public abstract void execute(VMScope scope, Task task)
			throws VMInternalException, VMExceptionOutOfMemory,
			VMExceptionFunctionNotFound;

	public abstract Code toCode();

	// Statements are closed and can't be inlined
	public String inlineCode() {
		return null;
	}
}
