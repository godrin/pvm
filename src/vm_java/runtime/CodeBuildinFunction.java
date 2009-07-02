package vm_java.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;
import vm_java.types.ObjectName;
import vm_java.types.VMExceptionFunctionNotFound;

@Deprecated
public class CodeBuildinFunction implements RuntimeFunction {
	Method mMethod;
	Object mObject;

	public CodeBuildinFunction(Object pObject, Method method)
			throws VMExceptionOutOfMemory, VMException {
		mMethod = method;
		mObject = pObject;
		if(mMethod==null)
			throw new VMException(null,"Method is not defined!");
	}

	@Override
	public void run(VMScope scope, ObjectName returnName,List<BasicObject> parameters,Task parentTask)
			throws VMException, VMExceptionOutOfMemory,
			VMExceptionFunctionNotFound {
		List<BasicObject> bos = RuntimeFunctionHelper.createArguments(scope,
				parameters);

		Class<?>[] signature = mMethod.getParameterTypes();

		Object[] args = RuntimeFunctionHelper.toJavaArgs(bos, signature);

		Object result;
		try {
			result = mMethod.invoke(mObject, args);
			BasicObject bo=RuntimeFunctionHelper.fromJava(scope.getContext(), result);
			scope.put(returnName, bo);
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
		//return new IntermedResult(null, Result.QUIT_EXCEPTION);
	}
/*
	@Override
	public void run(VMScope scope, ObjectName returnName,
			List<BasicObject> parameters) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		// TODO Auto-generated method stub
		
	}*/
}
