package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;
import vm_java.types.ObjectName;
import vm_java.types.VMBoolean;
import vm_java.types.VMExceptionFunctionNotFound;

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

}
