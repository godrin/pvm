package vm_java.code;

import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;

public class CodeClearVar extends CodeStatement {

	ObjectName varName;

	public CodeClearVar(VMContext context, SourceInfo source,
			ObjectName objectName) throws VMExceptionOutOfMemory {
		super(context, source);
		varName = objectName;
	}

	@Override
	public void execute(VMScope scope,Task parentTask) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		scope.clear(varName);
	}

	@Override
	public String inspect() {
		return "[Clear:"+varName.inspect()+"]";
	}

	@Override
	public Code toCode() {
		Code c=new Code();
		c.add("clear "+varName.inlineCode());
		return c;
	}
}
