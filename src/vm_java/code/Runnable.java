package vm_java.code;

import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.VMExceptionFunctionNotFound;

public interface Runnable {
	public void enqueue(VMScope scope);

	void go(VMScope scope) throws VMException, VMExceptionOutOfMemory, VMExceptionFunctionNotFound;
}
