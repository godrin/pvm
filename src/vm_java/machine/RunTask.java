package vm_java.machine;

import vm_java.code.Runnable;
import vm_java.code.VMException;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.VMExceptionFunctionNotFound;

public class RunTask extends Task {

	Runnable runnable;

	public RunTask(VMScope pScope, Runnable r) {
		super(pScope);
		runnable = r;
	}

	Runnable getStatement() {
		return runnable;
	}

	@Override
	public void run() throws VMException, VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		runnable.go(getScope());
	}

}
