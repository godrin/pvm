package vm_java.types;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import vm_java.code.VMException;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.types.basic.VMBuildinContextualModule;
import vm_java.types.basic.VMBuildinObjectBase;
import vm_java.types.basic.VMKlass;
import vm_java.types.basic.VMKlassBuiltin;
import vm_java.types.basic.VMModule;
import vm_java.types.basic.VMModuleBuiltin;
import vm_java.types.buildins.VMCtxLog;
import vm_java.types.buildins.VMIO;
import vm_java.types.buildins.VMRuntime;
import vm_java.types.foundation.VMArray;
import vm_java.types.foundation.VMHash;
import vm_java.types.foundation.VMInteger;
import vm_java.types.foundation.VMString;

public class Buildin {
	public static void createBuildins(VMScope scope) throws VMException,
			VMExceptionOutOfMemory {
		expose(scope, VMInteger.class);
		expose(scope, VMString.class);
		expose(scope, VMArray.class);
		expose(scope, VMHash.class);
		exposeSimpleModule(scope, VMIO.class);
		exposeModule(scope, VMCtxLog.class);
		expose(scope, VMRuntime.class);

		expose(scope, VMModule.class); // These must stand at the end !!! why
		// ?????
		expose(scope, VMKlass.class);
	}

	private static void exposeModule(VMScope scope,
			Class<? extends VMBuildinContextualModule> c) throws VMException {
		if (c.getAnnotation(DontExpose.class) != null) {
			throw new VMException(null, "Class " + c.toString()
					+ " should not be exposed!");
		}

		Constructor<? extends VMBuildinContextualModule> cnstr;
		try {
			cnstr = c.getConstructor(VMContext.class);
			VMBuildinContextualModule instance = cnstr.newInstance(scope
					.getContext());

			String name = c.getName().replaceAll(".*\\.", "");
			VMContext context = scope.getContext();

			VMModuleBuiltin k = new VMModuleBuiltin(context);
			k.setName(name);

			for (Method m : c.getMethods()) {

				if (m.getAnnotation(DontExpose.class) != null)
					continue;

				String mName = m.getName();
				// VMLog.debug("Expose:" + mName);
				BuildinFunction b = new BuildinFunction(context, m);
				if ((m.getModifiers() & Modifier.STATIC) != 0) {
					k.putStatic(context.intern(mName), b);
					k.putStatic(context.intern(shorten(mName)), b);
				} else {
					k.putInstance(context.intern(mName), b);
					k.putInstance(context.intern(shorten(mName)), b);
				}
			}

			scope.put(context.intern(name), k);
		} catch (Exception e) {
			throw new VMException(e);
		}

	}

	private static void exposeSimpleModule(VMScope scope, Class<VMIO> c)
			throws VMException, VMExceptionOutOfMemory {
		if (c.getAnnotation(DontExpose.class) != null) {
			throw new VMException(null, "Class " + c.toString()
					+ " should not be exposed!");
		}

		String name = c.getName().replaceAll(".*\\.", "");
		VMContext context = scope.getContext();

		VMModule k = new VMModule(context);
		k.setName(name);
		for (Method m : c.getMethods()) {

			if (m.getAnnotation(DontExpose.class) != null)
				continue;

			String mName = m.getName();
			// VMLog.debug("Expose:" + mName);
			BuildinFunction b = new BuildinFunction(context, m);
			if ((m.getModifiers() & Modifier.STATIC) != 0) {
				k.putStatic(context.intern(mName), b);
				k.putStatic(context.intern(shorten(mName)), b);
				k.putInstance(context.intern(mName), b);
				k.putInstance(context.intern(shorten(mName)), b);
			} else {
				VMLog.error("non static functions wihtin module ???  " + mName);
			}
		}

		scope.put(context.intern(name), k);

	}

	public static void expose(VMScope scope,
			Class<? extends VMBuildinObjectBase> c)
			throws VMExceptionOutOfMemory, VMException {
		if (c.getAnnotation(DontExpose.class) != null) {
			throw new VMException(null, "Class " + c.toString()
					+ " should not be exposed!");
		}

		String name = c.getName().replaceAll(".*\\.", "");
		VMContext context = scope.getContext();

		VMKlassBuiltin k = new VMKlassBuiltin(context);
		k.setName(name);
		context.setBuildinType(c.getName(), k);
		for (Method m : c.getMethods()) {

			if (m.getAnnotation(DontExpose.class) != null)
				continue;

			String mName = m.getName();
			// VMLog.debug("Expose:" + mName);
			BuildinFunction b = new BuildinFunction(context, m);
			if ((m.getModifiers() & Modifier.STATIC) != 0) {
				k.putStatic(context.intern(mName), b);
				k.putStatic(context.intern(shorten(mName)), b);
			} else {
				k.putInstance(context.intern(mName), b);
				k.putInstance(context.intern(shorten(mName)), b);
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
