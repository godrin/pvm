package vm_java.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import vm_java.code.VMInternalException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;

public class RuntimeFunctionHelper {

	public static List<BasicObject> createArguments(VMScope scope,
			List<? extends BasicObject> args)
			throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory,
			VMInternalException {
		List<BasicObject> n = new ArrayList<BasicObject>();

		for (BasicObject bo : args) {
			if (bo instanceof ObjectName) {
				bo = scope.get((ObjectName) bo);
			}
			n.add(bo);
		}

		return n;
	}

	public static Object[] toJavaArgs(Task pTask, List<BasicObject> bos,
			Class<?>[] signature) {
		Collection<?> margs = bos;
		Object[] os;
		
		VMScope pScope=pTask.getScope();
		int i = 0;
		if (signature[i] == VMContext.class) {
			os = new Object[(margs.size()) + 1];
			os[i] = pScope.getContext();
			i += 1;
		} else if (signature[i] == VMScope.class) {
			os = new Object[(margs.size()) + 1];
			os[i] = pScope;
			i += 1;
		} else if (signature[i] == Task.class) {
			os = new Object[(margs.size()) + 1];
			os[i] = pTask;
			i += 1;
		} else
			os = new Object[(margs.size())];

		for (Object o : margs) {
			Class<?> signaturKlass = signature[i];
			if (!signaturKlass.isInstance(o)) {
				// type mismatch
				if (o instanceof BasicObject) {
					// is a basicobject
					BasicObject bo = (BasicObject) o;
					Object tmp;
					tmp = bo.convertToJava(signaturKlass);
					if (tmp != null)
						o = tmp;
				}

			}
			os[i++] = o;
		}

		return os;
	}

	public static BasicObject fromJava(VMContext context, Object result)
			throws VMExceptionOutOfMemory {
		return BasicObject.convert(context, result);
	}
}
