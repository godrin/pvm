package vm_java.runtime;

import java.util.List;

import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;

public interface RuntimeFunction {
	
	void run(VMScope scope, ObjectName returnName,
			List<BasicObject> parameters, Task parentTask) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound;

}
