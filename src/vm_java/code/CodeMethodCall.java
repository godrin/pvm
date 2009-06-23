package vm_java.code;

import java.util.List;

import vm_java.code.lib.VMPackage;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.runtime.RuntimeFunction;
import vm_java.types.ObjectName;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.VMObject;

public class CodeMethodCall extends CodeStatement implements CodeExpression {

	ObjectName varName, methodName;
	List<ObjectName> parameters;

	public CodeMethodCall(VMContext context, ObjectName name, ObjectName name2,
			List<ObjectName> ps) throws VMExceptionOutOfMemory {
		super(context);
		varName = name;
		methodName = name2;
		parameters = ps;
	}

	@Override
	public IntermedResult execute(VMScope scope) throws VMException,
			VMExceptionFunctionNotFound, VMExceptionOutOfMemory {
		return compute(scope);
	}

	@Override
	public IntermedResult compute(VMScope scope)
			throws VMExceptionFunctionNotFound, VMExceptionOutOfMemory,
			VMException {
		// FIXME: add exception /checking
		RuntimeFunction f;
		if (varName == null) {
			f = scope.getFunction(methodName);
		} else {
			BasicObject bo = scope.get(varName);
			if (bo instanceof VMObject) {
				VMObject vmo = (VMObject) bo;

				f = vmo.getFunction(methodName);
			} else if (bo instanceof VMPackage) {
				VMPackage p = (VMPackage) bo;
				f = p.getFunction(methodName);
			}
			else
				//FIXME: return Quit_exception
				throw new VMException(this,"Function not found!");
		}
		return f.run(scope, parameters);
	}
}
