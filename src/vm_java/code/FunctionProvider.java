package vm_java.code;

import vm_java.context.VMExceptionOutOfMemory;
import vm_java.runtime.RuntimeFunction;
import vm_java.types.ObjectName;
import vm_java.types.VMExceptionFunctionNotFound;

public interface FunctionProvider {
	public RuntimeFunction getFunction(ObjectName name) throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory;

}
