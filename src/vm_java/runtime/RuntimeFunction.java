package vm_java.runtime;

import java.util.List;

import vm_java.code.IntermedResult;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.VMExceptionFunctionNotFound;

public interface RuntimeFunction {

	IntermedResult run(VMScope scope, List<BasicObject> parameters) throws VMException, VMExceptionOutOfMemory, VMExceptionFunctionNotFound;

}
