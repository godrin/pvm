package vm_java.code;

import java.util.List;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.llparser.ast.ASTReturn.Type;
import vm_java.machine.Task;
import vm_java.runtime.RuntimeFunction;
import vm_java.runtime.RuntimeFunctionHelper;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.VMExceptions;
import vm_java.types.basic.ObjectName;

public class CodeMethodCall extends CodeStatement {

	ObjectName varName, methodName;
	ObjectName returnName;
	List<ObjectName> parameters;
	boolean mstatic;

	public CodeMethodCall(VMContext context, SourceInfo source,
			ObjectName pReturnName, ObjectName objectName,
			ObjectName pMethodName, List<ObjectName> ps, boolean pstatic)
			throws VMExceptionOutOfMemory {
		super(context, source);
		mstatic = pstatic;
		varName = objectName;
		methodName = pMethodName;
		parameters = ps;
		returnName = pReturnName;
	}

	@Override
	public void execute(VMScope scope, Task pTask) throws VMException,
			VMExceptionOutOfMemory {
		try {

			RuntimeFunction f = null;
			BasicObject bo = null;
			if (varName == null) {
				f = scope.getFunction(methodName, mstatic);
			} else {
				bo = scope.get(varName, mstatic);
				if (bo instanceof FunctionProvider) {
					FunctionProvider vmo = (FunctionProvider) bo;

					f = vmo.getFunction(methodName);
				}
			}
			if (f == null) {
				pTask.setReturn(Type.EXCEPTION, scope.exception("NameError",
						"method not found:" + methodName + " in Object "
								+ varName, sourceInfo));
				return;
				/*
				 * VMLog.error(info()); VMLog.warn("Method not found:");
				 * VMLog.debug(toCode()); // VMLog.warn(scope.inspect());
				 * VMLog.debug("SELF:"); VMLog.debug(scope.self().inspect()); //
				 * FIXME: return Quit_exception throw new VMException(this,
				 * "Function " + methodName + " not found in " + varName + " ("
				 * + bo + "!");
				 */
			}

			List<BasicObject> bos = RuntimeFunctionHelper.createArguments(
					scope, parameters);
			f.run(scope, returnName, bos, pTask);
		} catch (VMExceptionFunctionNotFound e) {
			pTask.setReturn(Type.EXCEPTION, VMExceptions.vmException(
					getContext(), e));
		}

	}

	@Override
	public String inspect() {
		return "[CodeMethodCall:FIXME]";
	}

	@Override
	public Code toCode() {
		StringBuilder b = new StringBuilder();

		b.append(returnName.inlineCode()).append("=").append(
				varName.inlineCode()).append(".").append(
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
		Code c = new Code();
		c.add(b.toString());
		return c;
	}
}
