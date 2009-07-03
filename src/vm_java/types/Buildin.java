package vm_java.types;

import java.lang.reflect.Method;

import vm_java.VM;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;

public class Buildin {
	public static void createBuildins(VMScope scope) throws VMException, VMExceptionOutOfMemory {
		expose(scope,VMInteger.class);
		expose(scope,VMString.class);
		expose(scope,VMArray.class);
		expose(scope,VMIO.class);
	}
	
	public static void expose(VMScope scope,Class<? extends BasicObject> c) throws VMExceptionOutOfMemory, VMException {
		String name=c.getName().replaceAll(".*\\.","");
		VMContext context=scope.getContext();
		
		Klass k=new Klass(context);
		
		VMLog.debug("Exposing "+name);
		
		for(Method m:c.getMethods()) {
			String mName=m.getName();
			BuildinFunction b=new BuildinFunction(context,m);
			k.put(context.intern(mName), b);
			k.put(context.intern(shorten(mName)), b);
			VMLog.debug("Exposing method "+mName);
		}
		
		
		scope.put(context.intern(name),k);
		
	}

	private static String shorten(String name) {
		return name.replace("plus","+");
	}
	
	public static void main(String[] args) throws VMExceptionOutOfMemory, VMException {
		VM vm=new VM();
		VMContext ctx=vm.createContext();
		VMScope scope=ctx.createScope();
		Buildin.createBuildins(scope);
		
	}
}
