package vm_java.code;

import java.util.List;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.llparser.ast.ASTReturn.Type;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;

public class CodeTry extends CodeStatement {

	class TryTask extends Task {
		boolean ran = false;
		Task bodyTask = null;

		public TryTask(VMScope pScope, Task pParent) {
			super(pScope, pParent,null);
			// TODO Auto-generated constructor stub
		}

		@Override
		public String inspect() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void run() throws VMInternalException, VMExceptionOutOfMemory,
				VMExceptionFunctionNotFound {
			if (!ran) {
				getVM().addJob(this);
				getVM().addJob(bodyTask = block.execution(getScope(), this));
				ran = true;
			} else {

				if (bodyTask.getReturnType() == Type.EXCEPTION) {
					BasicObject exception = bodyTask.getReturnValue();

					for (CodeRescue rescue : rescues) {
						if (rescue.matches(getScope(), exception)) {
							// FIXME: check for further exceptions
							setReturn(null, null);
							getVM().addJob(this);
							getVM().addJob(
									bodyTask = rescue.execution(getScope(),
											this));
							return;
						}
					}
				}
				getParent().setReturn(getReturnType(), getReturnValue());
				finish(sourceInfo);
			}

		}

	}

	private List<CodeRescue> rescues;
	private CodeBlock block;

	public CodeTry(VMContext pContext, SourceInfo source, CodeBlock mblock,
			List<CodeRescue> mrescues) throws VMExceptionOutOfMemory {
		super(pContext, source);
		block = mblock;
		rescues = mrescues;
	}

	@Override
	public void execute(VMScope scope, Task parentTask) throws VMInternalException,
			VMExceptionOutOfMemory, VMExceptionFunctionNotFound {

		getVM().addJob(new TryTask(scope, parentTask));

	}

	@Override
	public Code toCode() {
		Code c = new Code();
		c.add("try");
		c.add(block.toCode().indent());
		for (CodeStatement r : rescues) {
			c.add(r.toCode());
		}
		c.add("end");
		return c;
	}

}
