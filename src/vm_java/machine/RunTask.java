package vm_java.machine;

import vm_java.code.Runnable;
import vm_java.code.VMInternalException;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.VMExceptionFunctionNotFound;

public class RunTask extends Task {

	Runnable runnable;
	boolean ran = false;

	public RunTask(VMScope pScope, Runnable r) {
		super(pScope);
		runnable = r;
	}

	public RunTask(VMScope pScope, Task pParent, Runnable r) {
		super(pScope, pParent,null);
		runnable = r;
	}

	Runnable getStatement() {
		return runnable;
	}

	@Override
	public void run() throws VMInternalException, VMExceptionOutOfMemory,
			VMExceptionFunctionNotFound {
		if (ran) {
			finish(null);
		} else {
			ran = true;
			runnable.go(getScope(), this);
			getVM().addJob(this);
		}
	}

	@Override
	public String inspect() {
		return "[RunTask:" + runnable + "]";
	}

}
