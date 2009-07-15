package vm_java.runtime;

import java.util.List;

import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.machine.Task;
import vm_java.types.Function;
import vm_java.types.VMModule;
import vm_java.types.ObjectName;
import vm_java.types.Reference;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.VMObject;

public class RuntimeMemberFunction implements RuntimeFunction {
	public static final String RETVALUE = "__retValue";

	private Function function;
	private VMModule module;
	private VMObject object;

	public RuntimeMemberFunction(VMModule pmodule, Function pFunction)
			throws VMException {
		module = pmodule;
		function = pFunction;
		if (function == null)
			throw new VMException(null, "function is null");
		assert (function != null);
	}

	public RuntimeMemberFunction(VMObject pObject, Function pFunction)
			throws VMException {
		function = pFunction;
		object = pObject;
		assert (function != null);
		if (function == null)
			throw new VMException(null, "function is null");
	}

	public void run(VMScope parentScope, ObjectName returnName,
			List<BasicObject> parameters, Task parentTask) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {

		List<BasicObject> bos = RuntimeFunctionHelper.createArguments(parentScope,
				parameters);

		VMScope subScope = createSubScope(parentScope);

		if (returnName != null) {
			subScope.put(subScope.getContext().intern(RETVALUE), new Reference(
					parentScope, returnName));
		}

		VMLog.debug("Running function:" + function);
		function.runFunction(subScope, returnName, bos, parentTask);
	}

	protected VMScope createSubScope(VMScope scope) throws VMException {
		VMScope subScope = null;
		if (object != null) {
			subScope = new VMScope(scope, object);
		} else {
			subScope = new VMScope(scope, module);
		}
		return subScope;
	}

}