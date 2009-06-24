package vm_java.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import vm_java.code.IntermedResult;
import vm_java.code.VMException;
import vm_java.code.IntermedResult.Result;
import vm_java.code.lib.PackageMethodNotFoundException;
import vm_java.code.lib.VMPackage;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.VMExceptionFunctionNotFound;

public class PackageFunction implements RuntimeFunction {
	VMPackage mPackage;

	public PackageFunction(VMPackage pPackage, VMContext pContext, String pName)
			throws VMExceptionOutOfMemory {
		mPackage = pPackage;
		mName = pName;

	}

	private Method getMethod() throws PackageMethodNotFoundException {
		System.out.println("Searching:" + mName);

		if (!mPackage.getFunctions().contains(mName))
			throw new PackageMethodNotFoundException(mName, mPackage);

		Class<? extends VMPackage> klass = mPackage.getClass();

		Method[] methods = klass.getMethods();

		for (Method method : methods) {
			if (method.getName().equals(mName)) {
				return method;
			}
		}
		return null;
	}

	String mName;

	@Override
	public IntermedResult run(VMScope scope, List<BasicObject> parameters)
			throws VMException, VMExceptionOutOfMemory,
			VMExceptionFunctionNotFound {
		// TODO Auto-generated method stub

		System.out.println("PackageFunction");
		Method method;
		Object result = null;
		boolean ok = false;

		try {
			method = getMethod();

			System.out.println("Found method:"+method);
			Collection<?> margs = parameters;
			Object[] os = new Object[(margs.size())];
			Class<?>[] signature = method.getParameterTypes();
			int i = 0;
			for (Object o : margs) {
				Class<?> signaturKlass = signature[i];
				if (!signaturKlass.isInstance(o)) {
					// type mismatch
					if (o instanceof BasicObject) {
						// is a basicobject
						BasicObject bo = (BasicObject) o;
						bo=bo.compute(scope).content();
						Object tmp;
						tmp = bo.convertToJava(signaturKlass, scope);
						if (tmp != null)
							o = tmp;
					}

				}
				os[i++] = o;
			}
			try {
				result = method.invoke(mPackage, os);
				ok = true;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (PackageMethodNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new VMException(null, "Unimplemented");
		}

		BasicObject bo = BasicObject.convert(scope.getContext(), result);

		return new IntermedResult(bo, Result.NONE);
	}
}