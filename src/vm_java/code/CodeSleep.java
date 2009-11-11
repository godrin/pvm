package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.machine.Task;
import vm_java.types.ObjectName;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.VMInteger;

public class CodeSleep extends CodeStatement {

	ObjectName mTimeName;

	public CodeSleep(VMContext pContext, SourceInfo source, ObjectName timeName)
			throws VMExceptionOutOfMemory {
		super(pContext, source);
		mTimeName = timeName;
	}

	@Override
	public void execute(VMScope scope, Task parentTask) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		BasicObject bo = scope.get(mTimeName);
		VMLog.debug("Sleep...");
		if (bo instanceof VMInteger) {
			VMInteger i = (VMInteger) bo;

			if (i.get() > 0) {
				parentTask.sleep(i.get());
			}
		}
	}

	@Override
	public Code toCode() {
		Code c = new Code();
		c.add("sleep " + mTimeName);
		return c;
	}

}
