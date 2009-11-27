/**
 * 
 */
package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.CodeWhile;
import vm_java.code.VMException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

/**
 * @author davidkamphausen
 * 
 */
public class ASTWhile extends AST implements ASTStatementInterface {

	ASTVar condition;
	ASTVar blockName;

	/**
	 * @param pSource
	 * @param blockName
	 * @param cond
	 */
	public ASTWhile(SourceInfo pSource, ASTVar cond, ASTVar pBlockName) {
		super(pSource);
		blockName = pBlockName;
		condition = cond;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * vm_java.llparser.ast.ASTStatementInterface#instantiate(vm_java.context
	 * .VMContext)
	 */
	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		return new CodeWhile(context, source, context.intern(condition.name),
				context.intern(blockName.name));
	}

}
