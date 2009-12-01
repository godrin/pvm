package vm_java.types.basic;

import vm_java.code.Code;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public abstract class VMBuildinObjectBase extends BasicObject {

	public VMBuildinObjectBase(VMContext pContext)
			throws VMExceptionOutOfMemory {
		super(pContext);
		// TODO Auto-generated constructor stub
	}



	public BasicObject getKlass() {
		return getContext().getBuildinType(this.getClass().getName());
	}

}
