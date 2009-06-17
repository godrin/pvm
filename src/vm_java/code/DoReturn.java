package vm_java.code;

import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.ObjectName;

public class DoReturn extends Statement {
	ObjectName objectName;

	public DoReturn(VMContext context, ObjectName pobjectName)
			throws VMExceptionOutOfMemory {
		super(context);
		objectName = pobjectName;
	}

	@Override
	public Result execute(VMScope scope) throws VMException {
		// TODO Auto-generated method stub
		scope.setReturn(scope.get(objectName));
		return Result.QUIT_FUNCTION;
	}

}
