/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code.lib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import vm_java.code.Function;
import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.context.VMContext;

/**
 * 
 * @author davidkamphausen
 */
public class VMPackage {

	class PackageFunction extends Function {

		PackageFunction(VMContext pContext, String pName)
				throws VMExceptionOutOfMemory {
			super(pContext);
			mName = pName;

		}

		@Override
		public void execute(VMScope pScope) {
			try {
				System.out.println("PackageFunction");
				Method method = getMethod();

				Map<String, BasicObject> args = pScope.getFuncCallArgs();
				Collection<?> margs = convertArgs(args);
				Object[] os = new Object[(margs.size())];
				Class[] pklasses = method.getParameterTypes();
				int i = 0;
				for (Object o : margs) {
					Class pk = pklasses[i];
					if (!pk.isInstance(o)) {
						if (o instanceof BasicObject) {
							BasicObject bo = (BasicObject) o;
							Object tmp;
							tmp = bo.convertTo(pk);
							if(tmp!=null)
								o=tmp;
						}

					}
					os[i++] = o;
				}

				try {
					method.invoke(VMPackage.this, os);
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

			} catch (MethodNotFoundException ex) {
				Logger.getLogger(VMPackage.class.getName()).log(Level.SEVERE,
						null, ex);
			}

		}

		private Collection<?> convertArgs(Map<String, BasicObject> args) {
			List<Object> x = new ArrayList<Object>();
			for (int i = 0; i < args.size(); i++) {
				x.add(args.get(Integer.toString(i)));
			}
			return x;
		}

		private Method getMethod() throws MethodNotFoundException {
			if (!mFunctions.contains(mName))
				throw new MethodNotFoundException(mName, VMPackage.this);

			Class klass = VMPackage.this.getClass();

			Method[] methods = klass.getMethods();

			for (Method method : methods) {
				if (method.getName().equals(mName)) {
					return method;
				}
			}
			return null;
		}

		String mName;
	}

	VMPackage() {
	}

	void addFunc(String pName) {
		mFunctions.add(pName);
	}

	public void init() {
	}

	public BasicObject get(VMContext pContext, String pName) {

		if (mFunctions.contains(pName)) {
			try {
				return new PackageFunction(pContext, pName);
			} catch (VMExceptionOutOfMemory ex) {
				Logger.getLogger(VMPackage.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
		return null;
	}

	public void registerPackage(VMScope pScope) {
		pScope.addPackage(this);
	}

	List<String> mFunctions = new ArrayList<String>();
}
