package vm_java.code;

import vm_java.context.VMExceptionOutOfMemory;
import vm_java.runtime.RuntimeFunction;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;

public interface FunctionProvider {
	public RuntimeFunction getFunction(ObjectName name) throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory;

}
