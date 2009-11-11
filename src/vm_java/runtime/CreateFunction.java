package vm_java.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.foundation.ObjectName;
import vm_java.types.foundation.VMCException;

public class CreateFunction implements RuntimeFunction {
	Class<? extends BasicObject> klass;

	public CreateFunction(Class<? extends BasicObject> _klass) {
		klass = _klass;
	}

	@Override
	public void run(VMScope scope, ObjectName returnName,
			List<BasicObject> parameters, Task parentTask) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		Constructor<? extends BasicObject> c;
		VMContext context=scope.getContext();
		try {
			c = klass.getConstructor(VMContext.class);
			BasicObject bo = c.newInstance(context);
			if (returnName != null)
				scope.put(returnName, bo);
		} catch (SecurityException e) {
			scope.setException(new VMCException(context,e));
		} catch (NoSuchMethodException e) {
			scope.setException(new VMCException(context,e));
		} catch (IllegalArgumentException e) {
			scope.setException(new VMCException(context,e));
		} catch (InstantiationException e) {
			scope.setException(new VMCException(context,e));
		} catch (IllegalAccessException e) {
			scope.setException(new VMCException(context,e));
		} catch (InvocationTargetException e) {
			scope.setException(new VMCException(context,e));
		}

	}

}
