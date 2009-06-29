package vm_java.machine;

import vm_java.code.Runnable;
import vm_java.context.VMScope;

public class Task {

	VMScope scope;
	Runnable runnable;

	public Task(Runnable r, VMScope pScope) {
		runnable=r;
		scope=pScope;
	}
	
	Runnable getStatement() {
		return runnable;
	}

	public VMScope getScope() {
		return scope;
	}

}
