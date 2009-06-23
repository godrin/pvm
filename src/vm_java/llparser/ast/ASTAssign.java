package vm_java.llparser.ast;

import vm_java.code.Assignment;
import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.VMException;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class ASTAssign extends AST implements ASTStatementInterface {
	public ASTVar left;
	public ASTRightValue right;

	public ASTAssign(ASTVar pleft, ASTRightValue pright) {
		left = pleft;
		right = pright;
	}

	@Override
	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		Assignment a = new Assignment(context, context.intern(left.name), 
				right
				.instantiate(context));
		return a;
	}
}
