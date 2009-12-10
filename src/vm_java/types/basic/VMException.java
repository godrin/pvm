package vm_java.types.basic;

import vm_java.code.Code;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class VMException extends VMBuildinObjectBase {

	public VMException(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}

	@Override
	public String inspect() {
		return "[VMCException]";
	}

	@Override
	public Code toCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String inlineCode() {
		// TODO Auto-generated method stub
		return null;
	}

}
