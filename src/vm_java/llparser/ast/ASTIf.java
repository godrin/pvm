package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeIf;
import vm_java.code.CodeStatement;
import vm_java.code.VMInternalException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class ASTIf extends AST implements ASTStatementInterface {
	ASTVar cond;
	ASTVar block;
	boolean wantedValue;

	public ASTIf(SourceInfo pSource, ASTVar condition, ASTVar blockName, boolean pWantedValue) {
		super(pSource);
		cond = condition;
		block = blockName;
		wantedValue=pWantedValue;
	}

	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMInternalException {
		return new CodeIf(context, source,
				context.intern(cond.name),
				context.intern(block.name),wantedValue);
	}

}
