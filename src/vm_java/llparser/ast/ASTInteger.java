package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.LocalAssignment;
import vm_java.code.VMException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.foundation.VMInteger;

public class ASTInteger extends AST implements ASTRightValue {
	int i;

	public ASTInteger(SourceInfo source, String input) {
		super(source);
		i = Integer.parseInt(input);
	}

	@Override
	public CodeStatement instantiate(VMContext context, ASTVar left)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		return new LocalAssignment(context, source, context.intern(left.name),
				new VMInteger(context, i));
	}
}
