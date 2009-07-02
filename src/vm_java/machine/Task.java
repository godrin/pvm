package vm_java.machine;

import java.util.ArrayList;
import java.util.List;

import vm_java.VM;
import vm_java.code.VMException;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.VMExceptionFunctionNotFound;

public abstract class Task {
	private VMScope scope;
	private List<Task> children = new ArrayList<Task>();;
	private Task parent;

	public Task(VMScope pScope) {
		scope = pScope;
		parent = null;
	}

	public Task(VMScope pScope, Task pParent) {
		scope = pScope;
		parent = pParent;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	public VMScope getScope() {
		return scope;
	}

	public boolean canResume() {
		return children.size() == 0;
	}
	
	public int childJobsCount() {
		return children.size();
	}

	public abstract void run() throws VMException, VMExceptionOutOfMemory,
			VMExceptionFunctionNotFound;

	private void addChild(Task pChild) {
		children.add(pChild);
	}

	private void removeChild(Task pChild) {
		children.remove(pChild);
	}

	protected void finish() throws VMException {
		if (children.size() > 0) {
			throw new VMException(null,
					"children.size is not null while finishing!");
		}
		if (parent != null) {
			parent.removeChild(this);
			parent = null;
		}
	}

	public VM getVM() {
		return scope.getContext().getVM();
	}

	public abstract String inspect();
}
