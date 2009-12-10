package vm_java.code;

import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;

public interface Runnable {

	public void go(VMScope scope, Task pParentTask) throws VMInternalException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound;
}
