package vm_java.types;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;

public class Buildin {
	public static void createBuildins(VMScope scope) throws VMException,
			VMExceptionOutOfMemory {
		expose(scope, VMInteger.class);
		expose(scope, VMString.class);
		expose(scope, VMArray.class);
		expose(scope, VMHash.class);
		expose(scope, VMIO.class);
		expose(scope, VMRuntime.class);
		
		expose(scope, VMModule.class); // These must stand at the end !!!
		expose(scope, VMKlass.class);
	}

	public static void expose(VMScope scope, Class<? extends BasicObject> c)
			throws VMExceptionOutOfMemory, VMException {
		if (c.getAnnotation(DontExpose.class) != null) {
			throw new VMException(null, "Class " + c.toString()
					+ " should not be exposed!");
		}

		String name = c.getName().replaceAll(".*\\.", "");
		VMContext context = scope.getContext();

		VMKlass k = new VMKlass(context);
		k.setJavaClass(c);
		VMLog.debug("Exposing klass:" + name);

		for (Method m : c.getMethods()) {

			if (m.getAnnotation(DontExpose.class) != null)
				continue;

			String mName = m.getName();
			VMLog.debug("Expose:" + mName);
			BuildinFunction b = new BuildinFunction(context, m);
			if ((m.getModifiers() & Modifier.STATIC) != 0) {
				k.putStatic(context.intern(mName), b);
				k.putStatic(context.intern(shorten(mName)), b);
			} else {
				k.put(context.intern(mName), b);
				k.put(context.intern(shorten(mName)), b);
			}
		}

		scope.put(context.intern(name), k);

	}

	private static String shorten(String name) {
		String sh = name.replace("plus", "+").replaceFirst("^_", "").replace(
				"lessThan", "<");
		if (!sh.equals(name)) {
			sh = sh;
		}
		return sh;
	}

}
