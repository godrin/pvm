package vm_java.code;

import vm_java.code.IntermedResult.Result;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.ObjectName;

public class DoReturn extends CodeStatement {
	ObjectName objectName;

	public DoReturn(VMContext context, SourceInfo source, ObjectName pobjectName)
			throws VMExceptionOutOfMemory {
		super(context, source);
		objectName = pobjectName;
	}

	@Override
	public IntermedResult execute(VMScope scope) throws VMException {
		// TODO Auto-generated method stub
		return new IntermedResult(scope.get(objectName), Result.QUIT_FUNCTION);
	}

}
