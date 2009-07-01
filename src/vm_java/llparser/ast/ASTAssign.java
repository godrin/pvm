package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.VMException;
import vm_java.code.CodeStatement.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.internal.VMLog;

public class ASTAssign extends AST implements ASTStatementInterface {

	ASTVar left;
	ASTRightValue rValue;

	public ASTAssign(SourceInfo source, ASTVar pLeft, ASTRightValue pRValue) {
		super(source);
		left = pLeft;
		rValue = pRValue;
	}

	@Override
	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		VMLog.debug(rValue);
		return rValue.instantiate(context,left);
	}

}
