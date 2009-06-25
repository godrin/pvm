package vm_java.types;

import java.lang.reflect.Method;

import vm_java.code.FunctionProvider;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.runtime.CodeBuildinFunction;
import vm_java.runtime.RuntimeFunction;

public class BuildInKlass extends BasicObject implements FunctionProvider{

	public BuildInKlass(VMContext context) throws VMExceptionOutOfMemory {
		super(context);
	}

	@Override
	public RuntimeFunction getFunction(ObjectName name) throws VMException,
			VMExceptionOutOfMemory {
		Method method=getMethodByName(name.toSymbolName());

		CodeBuildinFunction f=new CodeBuildinFunction(this,method);
		return f;
	}

	private Method getMethodByName(String name) {
		for(Method m:getClass().getMethods()) {
			if(m.getName().equals(name))
				return m;
		}
		System.out.println("ERROR: Method "+name+" not found in "+getClass().getName()+"!");
		return null;
	}

	private String className() {
		return getClass().getName().replaceAll(".*\\.","");
	}

}
