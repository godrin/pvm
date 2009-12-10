package vm_java.types.buildins;

import java.lang.reflect.Method;

import vm_java.code.FunctionProvider;
import vm_java.code.VMInternalException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.runtime.CodeBuildinFunction;
import vm_java.runtime.CreateFunction;
import vm_java.runtime.RuntimeFunction;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;

public abstract class BuildInKlass extends BasicObject implements FunctionProvider {

	public BuildInKlass(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}

	public static RuntimeFunction getStaticFunction(ObjectName name,
			Class<? extends BuildInKlass> klass) throws VMInternalException,
			VMExceptionOutOfMemory {

		if (name.getName().equals("new")) {
			return new CreateFunction(klass);
		}
		return null;
	}

	public RuntimeFunction getFunction(ObjectName name) throws VMExceptionFunctionNotFound,
			VMExceptionOutOfMemory {

		if (name.getName().equals("new")) {
			return new CreateFunction(this.getClass());
		}

		Method method = getMethodByName(name.toSymbolName());

		CodeBuildinFunction f = new CodeBuildinFunction(this, method);
		return f;
	}

	private Method getMethodByName(String name) {
		Method m = getMethodByPrivName(name);
		if (m != null)
			return m;
		String checked = checkName(name);
		if (!checked.equals(name))
			m = getMethodByName(checked);
		return m;
	}

	@Deprecated
	private String checkName(String name) {
		return name.replace("+", "plus").replace("-", "minus").replace("<",
				"lessThan");
	}

	private Method getMethodByPrivName(String name) {
		for (Method m : getClass().getMethods()) {
			if (m.getName().equals(name))
				return m;
		}
		return null;
	}

	@Override
	public String inspect() {
		return "[BuildInKlass]";
	}


}
