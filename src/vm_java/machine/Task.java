package vm_java.machine;

import java.util.ArrayList;
import java.util.List;

import vm_java.VM;
import vm_java.code.VMInternalException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.llparser.ast.ASTReturn.Type;
import vm_java.types.Reference;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.VMObject;
import vm_java.types.foundation.VMArray;
import vm_java.types.foundation.VMString;

public abstract class Task {
	private VMScope scope;
	private List<Task> children = new ArrayList<Task>();
	private Task parent;
	private long lastExecution;
	private long waitAtleastTil;
	private boolean farToLocal = false;

	private Type returned = null;
	private BasicObject returnContent = null;
	private Reference returnReference;

	public Task(VMScope pScope) {
		scope = pScope;
		parent = null;
	}

	public Task(VMScope pScope, Task pParent, Reference pReturnReference) {
		scope = pScope;
		returnReference = pReturnReference;
		parent = pParent;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	public void setFarToLocal() {
		farToLocal = true;
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

	public void go() throws VMInternalException, VMExceptionOutOfMemory,
			VMExceptionFunctionNotFound {
		run();
		lastExecution = System.currentTimeMillis();
	}

	protected abstract void run() throws VMInternalException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound;

	private void addChild(Task pChild) {
		children.add(pChild);
	}

	private void removeChild(Task pChild) {
		children.remove(pChild);
	}

	protected void finish(SourceInfo sourceInfo) throws VMInternalException,
			VMExceptionOutOfMemory {
		if (children.size() > 0) {
			throw new VMInternalException(null,
					"children.size is not null while finishing!");
		}
		if (parent != null) {
			if (getReturnType() != null) {
				if (getReturnType() == Type.EXCEPTION) {
					// add source-info as stacktrace
					VMContext ctx = getReturnValue().getContext();
					BasicObject bo = getReturnValue();
					if (bo instanceof VMObject) {
						VMObject vmo = (VMObject) getReturnValue();

						VMArray a = (VMArray) vmo.getInstance(ctx
								.intern("where"));
						if (sourceInfo != null) {

							a.add(new VMString(ctx, sourceInfo.toString()));
						}
						parent.setReturn(getReturnType(), getReturnValue());

					} else {
						VMLog
								.error("exception is not of internal type VMObject!");
					}
				} else if (getReturnType() == Type.FAR) {
					if (returnReference != null) {
						returnReference.set(getReturnValue());
					} else if (farToLocal)
						parent.setReturn(Type.LOCAL, getReturnValue());
					else
						parent.setReturn(getReturnType(), getReturnValue());
				} else if (getReturnType() == Type.LOCAL) {
					if (returnReference != null) {
						returnReference.set(getReturnValue());
					} else {
						parent.setReturn(getReturnType(), getReturnValue());
					}
				}
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
		if (returnType == Type.EXCEPTION) {
			if (getParent() == null) {
				VMLog.error("Exception :" + returnContent.inlineCode());
			}
		}
	}

	public Type getReturnType() {
		return returned;
	}

	public BasicObject getReturnValue() {
		return returnContent;
	}

}
