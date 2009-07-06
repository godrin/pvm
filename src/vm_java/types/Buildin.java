package vm_java.types;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import vm_java.VM;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;

public class Buildin {
	public static void createBuildins(VMScope scope) throws VMException,
			VMExceptionOutOfMemory {
		expose(scope, VMInteger.class);
		expose(scope, VMString.class);
		expose(scope, VMArray.class);
		expose(scope, VMHash.class);
		expose(scope, VMIO.class);
	}

	public static void expose(VMScope scope, Class<? extends BasicObject> c)
			throws VMExceptionOutOfMemory, VMException {
		String name = c.getName().replaceAll(".*\\.", "");
		VMContext context = scope.getContext();

		Klass k = new Klass(context);

		for (Method m : c.getMethods()) {
			String mName = m.getName();
			BuildinFunction b = new BuildinFunction(context, m);
			if ((m.getModifiers() & Modifier.STATIC)!=0) {
				k.putStatic(context.intern(mName),b);
				k.putStatic(context.intern(shorten(mName)),b);
			} else {
				k.put(context.intern(mName), b);
				k.put(context.intern(shorten(mName)), b);
			}
		}

		scope.put(context.intern(name), k);

	}

	private static String shorten(String name) {
		return name.replace("plus", "+").replaceFirst("^_","");
	}

	public static void main(String[] args) throws VMExceptionOutOfMemory,
			VMException {
		VM vm = new VM();
		VMContext ctx = vm.createContext();
		VMScope scope = ctx.createScope();
		Buildin.createBuildins(scope);

	}
}
