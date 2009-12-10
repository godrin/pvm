/**
 * 
 */
package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeInclude;
import vm_java.code.CodeStatement;
import vm_java.code.VMInternalException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

/**
 * @author davidkamphausen
 * 
 */
public class ASTInclude extends AST implements ASTStatementInterface {

	private ASTVar moduleName;

	/**
	 * @param moduleName
	 * @param sourceInfo
	 * 
	 */
	public ASTInclude(SourceInfo sourceInfo, ASTVar pmoduleName) {
		super(sourceInfo);
		moduleName = pmoduleName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * vm_java.llparser.ast.ASTStatementInterface#instantiate(vm_java.context
	 * .VMContext)
	 */
	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMInternalException {
		// TODO Auto-generated method stub
		return new CodeInclude(context, source, context.intern(moduleName.name));
	}

}
