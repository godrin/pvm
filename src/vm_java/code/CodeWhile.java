package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;
import vm_java.types.basic.ObjectName;
import vm_java.types.foundation.VMBoolean;

public class CodeWhile extends CodeStatement {

	class Execution extends Task {

		public Execution(VMScope pScope, Task pParent) {
			super(pScope, pParent);
		}

		@Override
		public String inspect() {
			return "[Execution of while]";
		}

		@Override
		protected void run() throws VMException, VMExceptionOutOfMemory,
				VMExceptionFunctionNotFound {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			BasicObject bo = getScope().get(blockName);
			CodeBlock b = null;
			VMBoolean cond = null;
			if (bo instanceof CodeBlock) {
				b = (CodeBlock) bo;
			}
			bo = getScope().get(conditionName);
			if (bo instanceof VMBoolean) {
				cond = (VMBoolean) bo;
			}

			if (cond == null) {
				throw new VMException(CodeWhile.this,
						"Condition is not defined");
			}
			if (b == null) {
				throw new VMException(CodeWhile.this, "Block is not defined");
			}

			// spawn somehow

			if (cond.get()) {
				getVM().addJob(b.execution(getScope(), this));
				getVM().addJob(this);
			} else {
				finish();
			}
		}

	}

	ObjectName conditionName;
	ObjectName blockName;
	boolean wantedValue;

	public CodeWhile(VMContext pContext, SourceInfo source,
			ObjectName pCondition, ObjectName pBlock)
			throws VMExceptionOutOfMemory {
		super(pContext, source);
		conditionName = pCondition;
		blockName = pBlock;
	}

	@Override
	public void execute(VMScope scope, Task parentTask) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {

		getVM().addJob(new Execution(scope, parentTask));
	}

	@Override
	public Code toCode() {
		Code c=new Code();
		if (wantedValue) {
			c.add("while " + conditionName + " do " + blockName);
		} else {
			c.add("until " + conditionName + " do " + blockName);
		}
		return c;
	}

}
