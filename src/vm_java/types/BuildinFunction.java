package vm_java.types;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import vm_java.code.Code;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.llparser.ast.ASTReturn.Type;
import vm_java.machine.Task;
import vm_java.runtime.RuntimeFunctionHelper;
import vm_java.types.basic.ObjectName;

/**
 * 
 * @author davidkamphausen
 * 
 *         This one is used for "normal" member functions (not the class
 *         functions)
 */
public class BuildinFunction extends Function {
	public Method method;

	public BuildinFunction(VMContext context, Method m)
			throws VMExceptionOutOfMemory {
		super(context);
		method = m;
	}

	public boolean isStatic() {
		return (method.getModifiers() & Modifier.STATIC) != 0;
	}

	public static BasicObject run(Task pTask, BasicObject self, Method method,
			List<BasicObject> bos) throws VMExceptionOutOfMemory {

		VMScope scope = pTask.getScope();
		Object[] args = RuntimeFunctionHelper.toJavaArgs(pTask, bos, method
				.getParameterTypes());
		Object result = null;

		try {
			result = method.invoke(self, args);
		} catch (IllegalArgumentException e) {
			pTask.setReturn(Type.EXCEPTION, VMExceptions.vmException(scope
					.getContext(), e));
			return null;
		} catch (IllegalAccessException e) {
			pTask.setReturn(Type.EXCEPTION, VMExceptions.vmException(scope
					.getContext(), e));
			return null;
		} catch (InvocationTargetException e) {
			pTask.setReturn(Type.EXCEPTION, VMExceptions.vmException(scope
					.getContext(), e));
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

		BasicObject r = run(parentTask, bo, method, bos);
		if (returnName != null) {
			scope.put(returnName, r);
		}
	}

	@Override
	public Code toCode() {
		return null;
	}

	@Override
	public String inlineCode() {
		return "<buildin function:" + method.getName() + ">";
	}

}
