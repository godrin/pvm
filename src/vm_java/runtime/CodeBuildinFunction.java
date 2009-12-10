package vm_java.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import vm_java.code.VMInternalException;
import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;

public class CodeBuildinFunction implements RuntimeFunction {
	Method mMethod;
	Object mObject;

	public CodeBuildinFunction(Object pObject, Method method)
			throws VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		mMethod = method;
		mObject = pObject;
		if (mMethod == null)
			throw new VMExceptionFunctionNotFound();
	}

	public void run(VMScope scope, ObjectName returnName,
			List<BasicObject> parameters, Task parentTask) throws VMInternalException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		List<BasicObject> bos = RuntimeFunctionHelper.createArguments(scope,
				parameters);

		Class<?>[] signature = mMethod.getParameterTypes();

		Object[] args = RuntimeFunctionHelper.toJavaArgs(parentTask, bos, signature);

		Object result;
		try {
			result = mMethod.invoke(mObject, args);
			BasicObject bo = RuntimeFunctionHelper.fromJava(scope.getContext(),
					result);
			if (returnName != null) {
				scope.put(returnName, bo);
			}
		} catch (IllegalArgumentException e) {
			VMLog.error(e);
		} catch (IllegalAccessException e) {
			VMLog.error(e);
		} catch (InvocationTargetException e) {
			VMLog.error(e);
		}
	}

}
