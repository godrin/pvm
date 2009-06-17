package vm_java.code;

import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.ObjectName;

public class Return extends Statement {

	ObjectName mName;

	Return(VMContext context, ObjectName pName) throws VMExceptionOutOfMemory {
		super(context);
		mName = pName;
	}

	@Override
	public Statement.Result execute(VMScope scope) throws VMException {
		scope.setReturn(scope.get(mName));
		return Result.NONE;
	}

}
