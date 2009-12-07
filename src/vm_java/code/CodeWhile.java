package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.llparser.ast.ASTReturn.Type;
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
			BasicObject bo = getScope().get(blockName);
			CodeBlock b = null;
			VMBoolean cond = null;

			// don't return local out of while !!!!
			if (getReturnType() == Type.LOCAL) {
				setReturn(null, null);
			}

			if (this.getReturnType() != null) {
				VMLog.debug("Foudn return:" + getReturnType());
				getParent().setReturn(getReturnType(), getReturnValue());
				finish(sourceInfo);
				return;
			}

			if (bo instanceof CodeBlock) {
				b = (CodeBlock) bo;
			} else {
				VMLog.error("No Block found in:" + bo + " (name:" + blockName
						+ ")");
			}
			bo = getScope().get(conditionName);
			if (bo instanceof VMBoolean) {
				cond = (VMBoolean) bo;
			}

			if (cond == null) {
				setReturn(Type.EXCEPTION, getScope().exception("NameError",
						"Condition " + conditionName.inspect() + " NOT found!",
						sourceInfo));
				return;
			}
			if (b == null) {
				throw new VMException(CodeWhile.this, "Block is not defined");
			}

			// spawn somehow

			VMLog.debug("cond value:" + cond.inspect() + " wanted:"
					+ wantedValue);

			if (cond.get() == wantedValue) {

				VMLog.debug("spawn subtask");
				getVM().addJob(b.execution(getScope(), this));
				getVM().addJob(this);
			} else {
				if (getReturnType() != null)
					getParent().setReturn(getReturnType(), getReturnValue());
				finish(sourceInfo);
			}
		}

	}

	private ObjectName conditionName;
	private ObjectName blockName;
	private boolean wantedValue;

	public CodeWhile(VMContext pContext, SourceInfo source,
			ObjectName pCondition, ObjectName pBlock, boolean pWantedValue)
			throws VMExceptionOutOfMemory {
		super(pContext, source);
		conditionName = pCondition;
		blockName = pBlock;
		wantedValue = pWantedValue;
	}

	@Override
	public void execute(VMScope scope, Task parentTask) throws VMException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {

		getVM().addJob(new Execution(scope, parentTask));
	}

	@Override
	public Code toCode() {
		Code c = new Code();
		if (wantedValue) {
			c.add("while " + conditionName + " do " + blockName);
		} else {
			c.add("until " + conditionName + " do " + blockName);
		}
		return c;
	}

}
