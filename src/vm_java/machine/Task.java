package vm_java.machine;

import vm_java.VM;
import vm_java.code.VMException;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.VMExceptionFunctionNotFound;

public abstract class Task {
	private VMScope scope;

	public Task(VMScope pScope) {
		scope = pScope;
	}
	public VMScope getScope() {
		return scope;
	}
	
	public abstract void run() throws VMException, VMExceptionOutOfMemory, VMExceptionFunctionNotFound;
	
	
	public void setReturn(Object retValue) {
		
	}
	
	public VM getVM() {
		return scope.getContext().getVM();
	}

}
