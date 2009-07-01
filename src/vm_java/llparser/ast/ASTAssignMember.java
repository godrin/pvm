package vm_java.llparser.ast;

import vm_java.code.LocalAssignment;
import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.MemberAssignment;
import vm_java.code.VMException;
import vm_java.code.CodeStatement.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.ObjectName;

public class ASTAssignMember extends AST implements ASTStatementInterface {
	public ASTVar left;
	public ASTVar leftMember;
	public ASTVar right;

	public ASTAssignMember(SourceInfo source, ASTVar pleft, ASTVar pleftmember,
			ASTVar pright) {
		super(source);
		left = pleft;
		right = pright;
		leftMember = pleftmember;
	}

	@Override
	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		if (leftMember != null) {
			return new MemberAssignment(context, source, context
					.intern(left.name), context.intern(leftMember.name),
					context.intern(right.name));
		} else {
			LocalAssignment a = new LocalAssignment(context, source, context
					.intern(left.name), context.intern(right.name));
			return a;
		}
	}
}