package vm_java.types;

import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.foundation.VMCException;

public class VMExceptionFunctionNotFound extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VMCException vm(VMContext context) throws VMExceptionOutOfMemory {
		return new VMCException(context,this);
	}

}
