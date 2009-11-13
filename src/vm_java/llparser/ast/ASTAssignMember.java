package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.LocalAssignment;
import vm_java.code.MemberAssignment;
import vm_java.code.VMException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class ASTAssignMember extends AST implements ASTStatementInterface {
	public ASTVar left;
	public ASTVar leftMember;
	public ASTVar right;
	private boolean mstatic; 

	public ASTAssignMember(SourceInfo source, ASTVar pleft, ASTVar pleftmember,
			ASTVar pright, boolean isstatic) {
		super(source);
		mstatic=isstatic;
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
					context.intern(right.name),mstatic);
		} else {
			LocalAssignment a = new LocalAssignment(context, source, context
					.intern(left.name), context.intern(right.name));
			return a;
		}
	}
}
