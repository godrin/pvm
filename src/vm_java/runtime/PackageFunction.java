package vm_java.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import vm_java.code.IntermedResult;
import vm_java.code.VMException;
import vm_java.code.IntermedResult.Result;
import vm_java.code.lib.PackageMethodNotFoundException;
import vm_java.code.lib.VMPackage;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.ObjectName;
import vm_java.types.VMExceptionFunctionNotFound;

public class PackageFunction implements RuntimeFunction {
	VMPackage mPackage;

	public PackageFunction(VMPackage pPackage, VMContext pContext, String pName)
			throws VMExceptionOutOfMemory {
		mPackage = pPackage;
		mName = pName;

	}

	public IntermedResult execute(VMScope pScope) throws VMExceptionOutOfMemory {
		boolean ok = false;
		Object result = null;
		try {
			System.out.println("PackageFunction");
			Method method = getMethod();

			Map<String, BasicObject> args = pScope.getFuncCallArgs();
			Collection<?> margs = convertArgs(args);
			Object[] os = new Object[(margs.size())];
			Class<?>[] pklasses = method.getParameterTypes();
			int i = 0;
			for (Object o : margs) {
				Class<?> pk = pklasses[i];
				if (!pk.isInstance(o)) {
					if (o instanceof BasicObject) {
						BasicObject bo = (BasicObject) o;
						Object tmp;
						tmp = bo.convertToJava(pk,pScope);
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

		} catch (PackageMethodNotFoundException ex) {
			Logger.getLogger(VMPackage.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		// FIXME
		if (ok)
			return new IntermedResult(BasicObject.convert(pScope.getContext(),
					result), Result.NONE);
		else
			return new IntermedResult(null, Result.QUIT_EXCEPTION);

	}

	private Collection<?> convertArgs(Map<String, BasicObject> args) {
		List<Object> x = new ArrayList<Object>();
		for (int i = 0; i < args.size(); i++) {
			x.add(args.get(Integer.toString(i)));
		}
		return x;
	}

	private Method getMethod() throws PackageMethodNotFoundException {
		System.out.println("Searching:"+mName);
		
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

	/*
	 * @Override public IntermedResult runFunction(VMScope scope, List<? extends
	 * BasicObject> args) throws VMException, VMExceptionOutOfMemory { throw new
	 * VMException(null, "Unimplemented"); }
	 */
	@Override
	public IntermedResult run(VMScope scope, List<ObjectName> parameters)
			throws VMException, VMExceptionOutOfMemory,
			VMExceptionFunctionNotFound {
		// TODO Auto-generated method stub

		System.out.println("PackageFunction");
		Method method;
		Object result = null;
		boolean ok=false;

		try {
			method = getMethod();

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
						Object tmp;
						tmp = bo.convertToJava(signaturKlass,scope);
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
		
		BasicObject bo=BasicObject.convert(scope.getContext(), result);

		return new IntermedResult(bo,Result.NONE);
	}
}