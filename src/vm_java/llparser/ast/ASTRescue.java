/**
 * 
 */
package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeRescue;
import vm_java.code.VMInternalException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.basic.ObjectName;

/**
 * @author davidkamphausen
 * 
 */
public class ASTRescue extends AST implements ASTStatementInterface {

	ASTBlock block;
	List<ASTVar> rescueTypes;
	ASTVar rescueVar;

	/**
	 * @param pSource
	 * @param types
	 * @param rescueFunction
	 */
	public ASTRescue(SourceInfo pSource, ASTBlock pblock, List<ASTVar> types,
			ASTVar prescueType) {
		super(pSource);
		block = pblock;
		rescueTypes = types;
		rescueVar = prescueType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * vm_java.llparser.ast.ASTStatementInterface#instantiate(vm_java.context
	 * .VMContext)
	 */
	public CodeRescue instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMInternalException {
		List<ObjectName> types = new ArrayList<ObjectName>();
		for (ASTVar v : rescueTypes) {
			vm_java.types.basic.ObjectName on = v.code(context);
			types.add(on);
		}
		if (rescueVar != null) {
			return new CodeRescue(context, source, types, rescueVar
					.code(context), block.instantiate(context));
		} else {
			return new CodeRescue(context, source, types, null, block
					.instantiate(context));
		}
	}

}
