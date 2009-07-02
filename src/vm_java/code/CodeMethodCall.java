package vm_java.code;

import java.util.List;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.machine.Task;
import vm_java.runtime.RuntimeFunction;
import vm_java.types.ObjectName;
import vm_java.types.VMExceptionFunctionNotFound;

public class CodeMethodCall extends CodeStatement {

	ObjectName varName, methodName;
	ObjectName returnName;
	List<BasicObject> parameters;

	public CodeMethodCall(VMContext context, SourceInfo source,
			ObjectName pReturnName, ObjectName objectName,
			ObjectName pMethodName, List<BasicObject> ps)
			throws VMExceptionOutOfMemory {
		super(context, source);
		varName = objectName;
		methodName = pMethodName;
		parameters = ps;
		returnName = pReturnName;
	}

	@Override
	public void execute(VMScope scope, Task parentTask) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {

		RuntimeFunction f = null;
		BasicObject bo = null;
		if (varName == null) {
			f = scope.getFunction(methodName);
		} else {
			bo = scope.get(varName);
			if (bo instanceof FunctionProvider) {
				FunctionProvider vmo = (FunctionProvider) bo;

				f = vmo.getFunction(methodName);
			}
		}
		if (f == null) {
			VMLog.error(info());
			VMLog.warn("Method not found:");
			VMLog.warn(scope.inspect());
			// FIXME: return Quit_exception
			throw new VMException(this, "Function " + methodName
					+ " not found in " + varName + " (" + bo + "!");
		}
		f.run(scope, returnName, parameters, parentTask);

	}

	@Override
	public String inspect() {
		return "[CodeMethodCall:FIXME]";
	}
}
