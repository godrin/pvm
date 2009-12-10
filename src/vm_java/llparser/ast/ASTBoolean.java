/**
 * 
 */
package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.LocalAssignment;
import vm_java.code.VMInternalException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.llparser.LineLexer2.Result;
import vm_java.types.foundation.VMBoolean;

/**
 * @author davidkamphausen
 * 
 */
public class ASTBoolean extends AST implements ASTRightValue {

	boolean b;

	/**
	 * @param pSource
	 * @param mResult
	 */
	public ASTBoolean(SourceInfo pSource, Result mResult) {
		super(pSource);
		b = mResult.getString().equals("true");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * vm_java.llparser.ast.ASTRightValue#instantiate(vm_java.context.VMContext,
	 * vm_java.llparser.ast.ASTVar)
	 */
	public CodeStatement instantiate(VMContext context, ASTVar left)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMInternalException {
		return new LocalAssignment(context, source, context.intern(left.name),
				new VMBoolean(context, b));
	}

}
