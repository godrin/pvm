package vm_java.types.basic;

import vm_java.code.Code;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class VMBuildinObjectBase extends BasicObject {

	public VMBuildinObjectBase(VMContext pContext)
			throws VMExceptionOutOfMemory {
		super(pContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String inlineCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String inspect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Code toCode() {
		// TODO Auto-generated method stub
		return null;
	}

	public BasicObject getKlass() {
		return getContext().getBuildinType(this.getClass().getName());
	}

}
