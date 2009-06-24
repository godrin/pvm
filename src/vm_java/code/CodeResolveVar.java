package vm_java.code;

import vm_java.code.IntermedResult.Result;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.ObjectName;
import vm_java.types.VMExceptionFunctionNotFound;

public class CodeResolveVar extends BasicObject implements CodeExpression{

	ObjectName mName;
	public CodeResolveVar(VMContext context,ObjectName pName) throws VMExceptionOutOfMemory {
		super(context);
		mName=pName;
	}
	
	@Override
	public IntermedResult compute(VMScope scope) throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory, VMException {
		return new IntermedResult(scope.get(mName),Result.NONE);
	}


}
