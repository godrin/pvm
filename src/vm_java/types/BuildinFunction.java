package vm_java.types;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;
import vm_java.runtime.RuntimeFunctionHelper;

public class BuildinFunction extends Function {
	Method method;

	public BuildinFunction(VMContext context, Method m)
			throws VMExceptionOutOfMemory {
		super(context);
		method = m;
	}

	public boolean isStatic() {
		return (method.getModifiers() & Modifier.STATIC) != 0;
	}

	public static BasicObject run(VMScope scope, BasicObject self,
			Method method, List<BasicObject> bos) throws VMExceptionOutOfMemory {

		Object[] args = RuntimeFunctionHelper.toJavaArgs(bos, method
				.getParameterTypes());

		Object[] os = new Object[bos.size()];
		for (int i = 0; i < bos.size(); i += 1) {
			os[i] = bos.get(i);
		}
		Object result = null;

		try {
			result = method.invoke(self, os);
		} catch (IllegalArgumentException e) {

			scope.setException(new VMCException(scope.getContext(), e));
			return null;
		} catch (IllegalAccessException e) {
			scope.setException(new VMCException(scope.getContext(), e));
			System.exit(1);
			return null;
		} catch (InvocationTargetException e) {
			scope.setException(new VMCException(scope.getContext(), e));
			System.exit(1);
			return null;
		}
		return BasicObject.convert(self.getContext(), result);
	}

	@Override
	public void runFunction(VMScope scope, ObjectName returnName,
			List<? extends BasicObject> args, Task parentTask)
			throws VMException, VMExceptionOutOfMemory,
			VMExceptionFunctionNotFound {
		BasicObject bo = scope.self();

		List<BasicObject> bos = RuntimeFunctionHelper.createArguments(scope,
				args);

		BasicObject r = run(scope, bo, method, bos);
		if (returnName != null) {
			scope.put(returnName, r);
		}
	}

}
