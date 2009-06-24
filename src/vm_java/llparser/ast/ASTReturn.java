package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.DoReturn;
import vm_java.code.VMException;
import vm_java.code.CodeStatement.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.ObjectName;

public class ASTReturn extends AST implements ASTStatementInterface {
	ASTVar mVar;

	public ASTReturn(SourceInfo source, ASTVar v) {
		super(source);
		mVar = v;
	}

	@Override
	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		ObjectName varName = context.intern(mVar.name);
		return new DoReturn(context, source, varName);
	}

}
