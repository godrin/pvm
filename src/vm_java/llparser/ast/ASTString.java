package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.LocalAssignment;
import vm_java.code.VMException;
import vm_java.code.CodeStatement.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.VMString;

public class ASTString extends AST implements ASTRightValue{
	String s;
	public ASTString(SourceInfo source,String str) {
		super(source);
		s=str;
	}
	@Override
	public CodeStatement instantiate(VMContext context, ASTVar left)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		return new LocalAssignment(context,source,
				context.intern(left.name),new VMString(context,s));
	}

}
