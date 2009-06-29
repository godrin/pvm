package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeClearVar;
import vm_java.code.CodeStatement;
import vm_java.code.VMException;
import vm_java.code.CodeStatement.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class ASTClearVar extends AST implements ASTStatementInterface {

	ASTVar var;
	public ASTClearVar(SourceInfo source,ASTVar v) {
		super(source);
		var=v;
	}

	@Override
	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		return new CodeClearVar(context,source,context.intern(var.name));
	}

}
