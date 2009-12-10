/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vm_java.types;

import java.util.List;

import vm_java.code.VMInternalException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;

/**
 * 
 * @author davidkamphausen
 */
public abstract class Function extends BasicObject {

	public Function(VMContext pContext) throws VMExceptionOutOfMemory {
		super(pContext);
	}



	public abstract void runFunction(VMScope pScope, Reference returnRef,
			List<? extends BasicObject> args, Task parentTask)
			throws VMInternalException, VMExceptionOutOfMemory,
			VMExceptionFunctionNotFound;

	public String inspect() {
		return "[FUNCTION:FIXME]";
	}

}
