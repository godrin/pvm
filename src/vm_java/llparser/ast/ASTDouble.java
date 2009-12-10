package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.LocalAssignment;
import vm_java.code.VMInternalException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.llparser.LineLexer2.Result;
import vm_java.types.foundation.VMDouble;

public class ASTDouble extends AST implements ASTRightValue {

	double doub;

	public ASTDouble(SourceInfo pSource, Result mResult) {
		super(pSource);
		doub = Double.parseDouble(mResult.getString());
	}

	public CodeStatement instantiate(VMContext context, ASTVar left)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMInternalException {
		return new LocalAssignment(context, source, context.intern(left.name),
				new VMDouble(context, doub));
	}

}
