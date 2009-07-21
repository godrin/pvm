/**
 * 
 */
package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.LocalAssignment;
import vm_java.code.VMException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

/**
 * @author davidkamphausen
 * 
 */
public class ASTRescue extends AST implements ASTStatementInterface {

	ASTVar resFunction;

	/**
	 * @param pSource
	 * @param rescueFunction
	 */
	public ASTRescue(SourceInfo pSource, ASTVar rescueFunction) {
		super(pSource);
		resFunction = rescueFunction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * vm_java.llparser.ast.ASTStatementInterface#instantiate(vm_java.context
	 * .VMContext)
	 */
	@Override
	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		return new LocalAssignment(context, source, context.internal("rescue"),
				context.intern(resFunction.name));
	}

}
