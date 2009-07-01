/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import java.util.ArrayList;
import java.util.List;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.machine.Task;
import vm_java.types.VMExceptionFunctionNotFound;

/**
 * 
 * @author davidkamphausen
 */
public class CodeBlock extends BasicObject implements LLTaskGenerator {

	static class Execution extends Task {
		int line;
		CodeBlock block;

		public Execution(CodeBlock codeBlock, VMScope pScope) {
			super(pScope);
			line = 0;
			block = codeBlock;
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
		public void run() throws VMException, VMExceptionOutOfMemory,
				VMExceptionFunctionNotFound {
			CodeStatement code = next();
			if (code != null) {

				code.execute(getScope());

				if(!getScope().finished())
					getVM().addJob(this);
			}

		}
	}

	List<CodeStatement> statements;
	private boolean isFinal = false;

	public CodeBlock(VMContext pContext) throws VMExceptionOutOfMemory {
		super(pContext);

		statements = new ArrayList<CodeStatement>();
	}

	public void add(CodeStatement pStatement) throws BlockIsFinalException,
			VMException {
		if (pStatement == null)
			throw new VMException(pStatement, "Statement is null");
		if (isFinal)
			throw new BlockIsFinalException();
		statements.add(pStatement);
	}

	public int size() {
		isFinal = true;
		return statements.size();
	}

	public Task execution(VMScope scope) {
		isFinal = true;
		return new Execution(this, scope);
	}

	@Override
	public String inspect() {
		return "[Block:"+statements.size()+" sts]";
	}
}
