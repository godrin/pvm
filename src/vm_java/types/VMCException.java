package vm_java.types;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class VMCException extends BasicObject {

	private Exception enclosed;
	
	public VMCException(VMContext context,Exception e) throws VMExceptionOutOfMemory {
		super(context);
		enclosed=e;
	}

	@Override
	public String inspect() {
		return "[VMCException]";
	}

}
