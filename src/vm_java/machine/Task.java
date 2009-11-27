package vm_java.machine;

import java.util.ArrayList;
import java.util.List;

import vm_java.VM;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.llparser.ast.ASTReturn.Type;
import vm_java.types.VMExceptionFunctionNotFound;

public abstract class Task {
	private VMScope scope;
	private List<Task> children = new ArrayList<Task>();
	private Task parent;
	private long lastExecution;
	private long waitAtleastTil;

	private Type returned = null;
	private BasicObject returnContent = null;

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

	public boolean canResume() throws VMFatalError {
		checkChildren();
		return children.size() == 0 && okToRun();
	}

	private void checkChildren() throws VMFatalError {
		for (Task child : children) {
			if (!getVM().hasJob(child)) {
				throw new VMFatalError("" + this + " has child " + child
						+ ", but it's not in job-list of vm!");
			}
		}
	}

	public int childJobsCount() {
		return children.size();
	}

	public Task getParent() {
		return parent;
	}

	public void go() throws VMException, VMExceptionOutOfMemory,
			VMExceptionFunctionNotFound {
		run();
		lastExecution = System.currentTimeMillis();
	}

	protected abstract void run() throws VMException, VMExceptionOutOfMemory,
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
			if (getReturnType() != null) {
				parent.setReturn(getReturnType(), getReturnValue());
			}

			parent.removeChild(this);
			parent = null;
		}
	}

	public void sleep(long millis) {
		if (waitAtleastTil > lastExecution) {
			waitAtleastTil += millis;
		} else {
			waitAtleastTil = lastExecution + millis;
		}
	}

	public boolean okToRun() {
		return (waitAtleastTil < System.currentTimeMillis());
	}

	public void setLastExecutionTime() {
		lastExecution = System.currentTimeMillis();
	}

	public VM getVM() {
		return scope.getContext().getVM();
	}

	public abstract String inspect();

	public void debugChildren() {
		VMLog.debug("ME:" + this);
		for (Task t : children) {
			VMLog.debug(t);
		}
	}
	
	
	public void setReturn(Type returnType, BasicObject pReturnContent) {
		returned = returnType;
		returnContent = pReturnContent;
	}
	public Type getReturnType() {
		return returned;
	}

	public BasicObject getReturnValue() {
		return returnContent;
	}


}
