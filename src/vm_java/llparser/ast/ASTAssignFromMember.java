/**
 * 
 */
package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeAssignmentFromMember;
import vm_java.code.CodeStatement;
import vm_java.code.VMException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

/**
 * @author davidkamphausen
 * 
 */
public class ASTAssignFromMember extends AST implements ASTRightValue {

	ASTVar rightVar, rightMember;

	/**
	 * @param pSource
	 * @param m
	 * @param v
	 */
	public ASTAssignFromMember(SourceInfo pSource, ASTVar v, ASTVar m) {
		super(pSource);
		rightVar = v;
		rightMember = m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * vm_java.llparser.ast.ASTRightValue#instantiate(vm_java.context.VMContext,
	 * vm_java.llparser.ast.ASTVar)
	 */
	@Override
	public CodeStatement instantiate(VMContext context, ASTVar left)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		return new CodeAssignmentFromMember(context, source, context
				.intern(left.name), context.intern(rightVar.name), context
				.intern(rightMember.name));
	}

}
