package vm_java.types.basic;

import vm_java.code.FunctionProvider;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.runtime.RuntimeFunction;
import vm_java.types.VMExceptionFunctionNotFound;

public abstract class VMBuildinObjectBase extends BasicObject implements
		FunctionProvider {

	public VMBuildinObjectBase(VMContext pContext)
			throws VMExceptionOutOfMemory {
		super(pContext);
	}

	public BasicObject getKlass() {
		return getContext().getBuildinType(this.getClass().getName());
	}

	public RuntimeFunction getFunction(ObjectName name)
			throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory {
		VMKlass k = (VMKlass) getKlass();
		return k.getInstanceFunction(name,this);
	}
}
