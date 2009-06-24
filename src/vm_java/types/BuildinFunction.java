package vm_java.types;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import vm_java.code.IntermedResult;
import vm_java.code.VMException;
import vm_java.code.IntermedResult.Result;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.runtime.RuntimeFunctionHelper;

public class BuildinFunction extends Function {
	Method method;

	public BuildinFunction(VMContext context, Method m)
			throws VMExceptionOutOfMemory {
		super(context);
		method = m;
	}

	@Override
	public IntermedResult runFunction(VMScope scope,
			List<? extends BasicObject> args) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		BasicObject bo=scope.self();
		
		if(bo instanceof BuildInInterface) {
			BuildInInterface i=(BuildInInterface)bo;
			
			List<BasicObject> bos=RuntimeFunctionHelper.createArguments(scope, args);
			
			IntermedResult res=run(bo,method,bos);
			return res;
		} else {
			return new IntermedResult(BasicObject.nil,Result.QUIT_EXCEPTION);
		}
	}
	
	public static IntermedResult run(BasicObject self,Method method, List<BasicObject> bos) throws VMExceptionOutOfMemory {
		Object[] os=new Object[bos.size()];
		for(int i=0;i<bos.size();i+=1) {
			os[i]=bos.get(i);
		}
		Object result=null;
		
		try {
			result=method.invoke(self, os);
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
		BasicObject bo = BasicObject.convert(self.getContext(), result);

		return new IntermedResult(bo,Result.NONE);
	}


}
