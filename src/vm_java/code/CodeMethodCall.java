package vm_java.code;

import java.util.List;

import vm_java.code.IntermedResult.Result;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.runtime.RuntimeFunction;
import vm_java.types.ObjectName;
import vm_java.types.VMExceptionFunctionNotFound;

public class CodeMethodCall extends CodeStatement implements CodeExpression {

	ObjectName varName, methodName;
	List<BasicObject> parameters;

	public CodeMethodCall(VMContext context, SourceInfo source,
			ObjectName name, ObjectName name2, List<BasicObject> ps)
			throws VMExceptionOutOfMemory {
		super(context, source);
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
		RuntimeFunction f = null;
		BasicObject bo = null;
		if (varName == null) {
			f = scope.getFunction(methodName);
		} else {
			bo = scope.get(varName);
			VMLog.debug("COMP:" + bo);
			if (bo instanceof FunctionProvider) {
				VMLog.debug("is funcprov:" + bo);
				FunctionProvider vmo = (FunctionProvider) bo;

				f = vmo.getFunction(methodName);
			}
		}
		if (f == null) {
			// FIXME: return Quit_exception
			throw new VMException(this, "Function " + methodName
					+ " not found in " + varName + " (" + bo + "!");
		}
		IntermedResult res = f.run(scope, parameters);

		if (res.returned()) {
			return new IntermedResult(res.content(), Result.NONE);
		} else
			return res;
	}
}
