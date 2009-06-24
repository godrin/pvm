package vm_java.runtime;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.VMException;
import vm_java.context.BasicObject;
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
}
