package vm_java.code;

import java.util.List;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.machine.Task;
import vm_java.runtime.RuntimeFunction;
import vm_java.runtime.RuntimeFunctionHelper;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.foundation.ObjectName;

public class CodeMethodCall extends CodeStatement {

	ObjectName varName, methodName;
	ObjectName returnName;
	List<ObjectName> parameters;

	public CodeMethodCall(VMContext context, SourceInfo source,
			ObjectName pReturnName, ObjectName objectName,
			ObjectName pMethodName, List<ObjectName> ps)
			throws VMExceptionOutOfMemory {
		super(context, source);
		varName = objectName;
		methodName = pMethodName;
		parameters = ps;
		returnName = pReturnName;
	}

	@Override
	public void execute(VMScope scope, Task parentTask) throws VMException,
			VMExceptionOutOfMemory {
		try {

			RuntimeFunction f = null;
			BasicObject bo = null;
			if (varName == null) {
				f = scope.getFunction(methodName);
			} else {
				bo = scope.get(varName);
				if (bo instanceof FunctionProvider) {
					FunctionProvider vmo = (FunctionProvider) bo;

					f = vmo.getFunction(methodName);
				}
			}
			if (f == null) {
				VMLog.error(info());
				VMLog.warn("Method not found:");
				VMLog.warn(scope.inspect());
				VMLog.debug("SELF:");
				VMLog.debug(scope.self().inspect());
				// FIXME: return Quit_exception
				throw new VMException(this, "Function " + methodName
						+ " not found in " + varName + " (" + bo + "!");
			}

			List<BasicObject> bos = RuntimeFunctionHelper.createArguments(scope,
					parameters);
			f.run(scope, returnName, bos, parentTask);
		} catch (VMExceptionFunctionNotFound e) {
			scope.setException(e.vm(getContext()));
		}

	}

	@Override
	public String inspect() {
		return "[CodeMethodCall:FIXME]";
	}

	@Override
	public Code toCode() {
		StringBuilder b = new StringBuilder();

		b.append(returnName.inlineCode()).append("=").append(varName.inlineCode()).append(".").append(
				methodName.inlineCode()).append("(");

		boolean first = true;
		for (ObjectName bo : parameters) {
			if (first) {
				first = false;
			} else {
				b.append(", ");
			}
			b.append(bo.inlineCode());
		}
		b.append(")");
		Code c=new Code();
		c.add(b.toString());
		return c;
	}
}
