package vm_java.runtime;

import java.util.List;

import vm_java.code.IntermedResult;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.types.Function;
import vm_java.types.Module;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.VMObject;

public class MemberFunction implements RuntimeFunction{
	private Function function;
	private Module module;
	private VMObject object;

	public MemberFunction(Module pmodule, Function pFunction) throws VMException {
		module = pmodule;
		function = pFunction;
		if(function==null)
			throw new VMException(null,"function is null");
		assert(function!=null);
	}

	public MemberFunction(VMObject pObject, Function pFunction) throws VMException {
		if(function==null)
			throw new VMException(null,"function is null");
		function = pFunction;
		object = pObject;
		assert(function!=null);
	}

	public IntermedResult run(VMScope scope, List<BasicObject> parameters) throws VMException, VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		VMScope subScope;

		if (object != null) {
			subScope = new VMScope(scope, object);
		} else {
			subScope = new VMScope(scope, module);
		}
		List<BasicObject> bos=RuntimeFunctionHelper.createArguments(scope,parameters);
		
		VMLog.debug("Running function:"+function);
		IntermedResult res=function.runFunction(subScope, bos);
		
		return res;
	}
}
