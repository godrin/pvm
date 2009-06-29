package vm_java.code;

import vm_java.context.VMScope;

public interface Runnable {
	public void enqueue(VMScope scope);

	public void go();
}
