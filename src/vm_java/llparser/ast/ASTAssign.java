package vm_java.llparser.ast;

import vm_java.code.Assignment;
import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.MemberAssignment;
import vm_java.code.VMException;
import vm_java.code.CodeStatement.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class ASTAssign extends AST implements ASTStatementInterface {
	public ASTVar left;
	public ASTVar leftMember;
	public ASTRightValue right;

	public ASTAssign(SourceInfo source,ASTVar pleft, ASTRightValue pright) {
		super(source);
		left = pleft;
		right = pright;
	}

	public ASTAssign(SourceInfo source,ASTVar pleft, ASTVar pleftmember, ASTRightValue pright) {
		super(source);
		left = pleft;
		right = pright;
		leftMember = pleftmember;
	}

	@Override
	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		if (leftMember != null) {
			return new MemberAssignment(context, source, context.intern(left.name),
					context.intern(leftMember.name), right.instantiate(context));
		} else {
			Assignment a = new Assignment(context, source, context.intern(left.name),
					right.instantiate(context));
			return a;
		}
	}
}
