package vm_java.runtime;

import java.lang.reflect.Method;
import java.util.List;

import vm_java.code.IntermedResult;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.VMExceptionFunctionNotFound;

@Deprecated

public class CodeBuildinFunction implements RuntimeFunction {
	Method mMethod;
	Object mObject;

	public CodeBuildinFunction(Object pObject,Method method) throws VMExceptionOutOfMemory {
		mMethod=method;
		mObject=pObject;
	}


	@Override
	public IntermedResult run(VMScope scope, List<BasicObject> parameters)
			throws VMException, VMExceptionOutOfMemory,
			VMExceptionFunctionNotFound {
		// TODO Auto-generated method stub
		return null;
	}
}
