package vm_java.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import vm_java.code.IntermedResult;
import vm_java.code.VMException;
import vm_java.code.IntermedResult.Result;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.VMExceptionFunctionNotFound;

public class RuntimeFunctionHelper {
	
	public static List<BasicObject> createArguments(VMScope scope, List<? extends BasicObject> args)
			throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory, VMException {
		List<BasicObject> n=new ArrayList<BasicObject>();
		
		for(BasicObject bo:args) {
			n.add(bo.compute(scope).content());
		}
		
		return n;
	}

	public static Object[] toJavaArgs(List<BasicObject> bos,Class<?>[] signature) {
		Collection<?> margs = bos;
		Object[] os = new Object[(margs.size())];
		int i = 0;
		for (Object o : margs) {
			Class< ? > signaturKlass = signature[i];
			System.out.println("Converting "+o+" to "+signaturKlass);
			if (!signaturKlass.isInstance(o)) {
				// type mismatch
				if (o instanceof BasicObject) {
					// is a basicobject
					BasicObject bo = (BasicObject) o;
					Object tmp;
					tmp = bo.convertToJava(signaturKlass);
					if (tmp != null)
						o = tmp;
				}

			}
			os[i++] = o;
		}

		return os;
	}

	public static IntermedResult fromJava(VMContext context,Object result) throws VMExceptionOutOfMemory {
		BasicObject bo=BasicObject.convert(context, result);
		return new IntermedResult(bo,Result.NONE);
	}
}
