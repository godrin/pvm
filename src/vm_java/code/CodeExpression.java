package vm_java.code;

import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.VMExceptionFunctionNotFound;

public interface CodeExpression {

	IntermedResult compute(VMScope scope) throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory, VMException;

}
