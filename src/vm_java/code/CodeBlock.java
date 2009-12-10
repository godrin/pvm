/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import java.util.ArrayList;
import java.util.List;

import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;
import vm_java.types.Reference;
import vm_java.types.VMExceptionFunctionNotFound;

/**
 * 
 * @author davidkamphausen
 */
public class CodeBlock extends SourceBased implements LLTaskGenerator {

	static class Execution extends Task {
		int line;
		CodeBlock block;

		public Execution(CodeBlock codeBlock, VMScope pScope, Task parentTask) {
			super(pScope, parentTask, null);
			line = 0;
			block = codeBlock;
		}

		public Execution(CodeBlock codeBlock, VMScope scope, Task parentTask,
				Reference returnReference) {
			super(scope, parentTask, returnReference);
			line = 0;
			block = codeBlock;
		}

		private CodeStatement current() {
			if (line >= 0 && line < block.statements.size())
				return block.statements.get(line);
			return null;
		}

		private CodeStatement next() {
			CodeStatement cs = null;

			if (hasNext())
				cs = block.statements.get(line);
			line += 1;
			return cs;
		}

		private boolean hasNext() {
			return line < block.statements.size();
		}

		@Override
		public void run() throws VMInternalException, VMExceptionOutOfMemory,
				VMExceptionFunctionNotFound {
			if (finished()) {
				if (current() != null)
					finish(current().sourceInfo);
				finish(null);
				return;
			}
			CodeStatement code = next();
			if (code != null) {

				code.execute(getScope(), this);

				getVM().addJob(this);
			} else {
				finish(null); // code.sourceInfo);
			}

		}

		private boolean finished() {
			return getReturnType() != null;
		}

		@Override
		public String inspect() {
			if (line < block.statements.size()) {
				return "[CodeBlockTask:" + block.statements.get(line).info()
						+ "]";
			} else {
				return "[CodeBlockTask: at end]";

			}
		}
	}

	List<CodeStatement> statements;
	private boolean isFinal = false;

	public CodeBlock(VMContext pContext, SourceInfo source)
			throws VMExceptionOutOfMemory {
		super(pContext, source);

		statements = new ArrayList<CodeStatement>();
	}

	public void add(CodeStatement pStatement) throws BlockIsFinalException,
			VMInternalException {
		if (pStatement == null)
			throw new VMInternalException(pStatement, "Statement is null");
		if (isFinal)
			throw new BlockIsFinalException();
		statements.add(pStatement);
	}

	public int size() {
		isFinal = true;
		return statements.size();
	}

	public Task execution(VMScope scope, Task parentTask) {
		isFinal = true;
		return new Execution(this, scope, parentTask);
	}

	@Override
	public String inspect() {
		return "[Block:" + statements.size() + " sts =" + super.inspect() + "]";
	}

	public Code toCode() {
		Code c = new Code();
		for (CodeStatement cs : statements) {
			c.add(cs.toCode());
		}
		return c;
	}

	@Override
	public String inlineCode() {
		return "begin\n" + toCode().indent().toString() + "end\n";
	}

	public Task execution(VMScope scope, Task parentTask, Reference returnName) {
		isFinal = true;
		return new Execution(this, scope, parentTask, returnName);
	}
}
