package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.VMInternalException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.internal.VMLog;

public class ASTAssign extends AST implements ASTStatementInterface {

	ASTVar left;
	ASTRightValue rValue;

	public ASTAssign(SourceInfo source, ASTVar pLeft, ASTRightValue pRValue) {
		super(source);
		if (pRValue == null) {
			throw new NullPointerException();
		}
		left = pLeft;
		rValue = pRValue;
	}

	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMInternalException {
		VMLog.debug(rValue);
		if (rValue != null) {
			return rValue.instantiate(context, left);
		} else {
			throw new NullPointerException();
		}
	}

}
