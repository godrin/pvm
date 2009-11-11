package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.VMModule;
import vm_java.types.foundation.ObjectName;

public class CodeInclude extends CodeStatement {

	ObjectName moduleName;

	public CodeInclude(VMContext pContext, SourceInfo source,
			ObjectName pmoduleName) throws VMExceptionOutOfMemory {
		super(pContext, source);
		moduleName = pmoduleName;
	}

	@Override
	public void execute(VMScope scope, Task parentTask) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		// TODO Auto-generated method stub
		BasicObject bo = scope.self();
		if (bo instanceof VMModule) {
			VMModule mod = (VMModule) bo;
			mod.include((VMModule) scope.get(moduleName));
		} else {
			VMLog.error("No include in " + bo);
		}
	}

	@Override
	public Code toCode() {
		Code c = new Code();
		c.add("include " + moduleName.inlineCode());

		return c;
	}

}
