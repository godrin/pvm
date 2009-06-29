package vm_java.code;

import vm_java.code.IntermedResult.Result;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.ObjectName;
import vm_java.types.VMExceptionFunctionNotFound;

public class CodeClearVar extends CodeStatement {

	ObjectName varName;

	public CodeClearVar(VMContext context, SourceInfo source,
			ObjectName objectName) throws VMExceptionOutOfMemory {
		super(context, source);
		varName = objectName;
	}

	@Override
	public IntermedResult execute(VMScope scope) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		IntermedResult res = varName.compute(scope);
		BasicObject bo = res.content();
		if (bo instanceof ObjectName) {
			scope.clear((ObjectName) bo);
		}
		return new IntermedResult(null, Result.NONE);
	}

}
