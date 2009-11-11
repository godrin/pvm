package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.foundation.ObjectName;
import vm_java.types.foundation.VMBoolean;

public class CodeIf extends CodeStatement {

	ObjectName conditionName;
	ObjectName blockName;
	boolean wantedValue;

	public CodeIf(VMContext pContext, SourceInfo source, ObjectName pCondition,
			ObjectName pBlock, boolean pWantedValue)
			throws VMExceptionOutOfMemory {
		super(pContext, source);
		conditionName = pCondition;
		blockName = pBlock;
		wantedValue = pWantedValue;
	}

	@Override
	public void execute(VMScope scope, Task parentTask) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
		// TODO Auto-generated method stub
		BasicObject bo = scope.get(blockName);
		CodeBlock b = null;
		VMBoolean cond = null;
		if (bo instanceof CodeBlock) {
			b = (CodeBlock) bo;
		}
		bo = scope.get(conditionName);
		if (bo instanceof VMBoolean) {
			cond = (VMBoolean) bo;
		}

		if (cond == null) {
			throw new VMException(this, "Condition is not defined");
		}
		if (b == null) {
			throw new VMException(this, "Block is not defined");
		}

		if (cond.get() == wantedValue) {
			getVM().addJob(b.execution(scope, parentTask));

		}
	}

	@Override
	public Code toCode() {
		Code c = new Code();
		if (wantedValue) {
			c.add("if " + conditionName + " then " + blockName);
		} else {
			c.add("unless " + conditionName + " then " + blockName);
		}
		return c;
	}

}
