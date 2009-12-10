package vm_java.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import vm_java.code.VMInternalException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.llparser.ast.ASTReturn.Type;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.VMExceptions;
import vm_java.types.basic.ObjectName;

public class CreateFunction implements RuntimeFunction {
	Class<? extends BasicObject> klass;

	public CreateFunction(Class<? extends BasicObject> _klass) {
		klass = _klass;
	}

	public void run(VMScope scope, ObjectName returnName,
			List<BasicObject> parameters, Task pTask) throws VMInternalException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		Constructor<? extends BasicObject> c;
		VMContext context=scope.getContext();
		try {
			c = klass.getConstructor(VMContext.class);
			BasicObject bo = c.newInstance(context);
			if (returnName != null)
				scope.put(returnName, bo);
		} catch (SecurityException e) {
			pTask.setReturn(Type.EXCEPTION, VMExceptions.vmException(context, e));
		} catch (NoSuchMethodException e) {
			pTask.setReturn(Type.EXCEPTION, VMExceptions.vmException(context, e));
		} catch (IllegalArgumentException e) {
			pTask.setReturn(Type.EXCEPTION, VMExceptions.vmException(context, e));
		} catch (InstantiationException e) {
			pTask.setReturn(Type.EXCEPTION, VMExceptions.vmException(context, e));
		} catch (IllegalAccessException e) {
			pTask.setReturn(Type.EXCEPTION, VMExceptions.vmException(context, e));
		} catch (InvocationTargetException e) {
			pTask.setReturn(Type.EXCEPTION, VMExceptions.vmException(context, e));
		}

	}

}
