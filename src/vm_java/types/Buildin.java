package vm_java.types;

import java.lang.reflect.Method;

import vm_java.code.VMException;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;

public class Buildin {
	public static void createBuildins(VMScope scope) throws VMException, VMExceptionOutOfMemory {
		expose(scope,VMInteger.class);
	}
	
	public static void expose(VMScope scope,Class c) throws VMExceptionOutOfMemory, VMException {
		String name=c.getName().replaceAll(".*\\.","");
		VMContext context=scope.getContext();
		
		Klass k=new Klass(context);
		
		for(Method m:c.getMethods()) {
			String mName=m.getName();
			BuildinFunction b=new BuildinFunction(context,m);
			k.put(context.intern(mName), b);
		}
		
		scope.put(context.intern(name),k);
		
	}
}
