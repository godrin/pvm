package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeSleep;
import vm_java.code.CodeStatement;
import vm_java.code.VMInternalException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class ASTSleep extends AST implements ASTStatementInterface {
	ASTVar time;

	public ASTSleep(SourceInfo pSource, ASTVar v) {
		super(pSource);
		time = v;
	}


	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMInternalException {
		return new CodeSleep(context, source, context.intern(time.name));
	}

}
