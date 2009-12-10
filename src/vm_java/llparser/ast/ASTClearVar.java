package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeClearVar;
import vm_java.code.CodeStatement;
import vm_java.code.VMInternalException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class ASTClearVar extends AST implements ASTStatementInterface {

	ASTVar var;
	public ASTClearVar(SourceInfo source,ASTVar v) {
		super(source);
		var=v;
	}

	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMInternalException {
		return new CodeClearVar(context,source,context.intern(var.name));
	}

}
