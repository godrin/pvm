package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.LocalAssignment;
import vm_java.code.VMException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.basic.ObjectName;

public class ASTVar extends AST implements ASTRightValue {
	public String name;

	public ASTVar(SourceInfo source,String pname) {
		super(source);
		name = pname;
	}

	public ObjectName code(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		return context.intern(name);
	}

	@Override
	public CodeStatement instantiate(VMContext context, ASTVar left)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		return new LocalAssignment(context,source,
				context.intern(left.name),context.intern(name));
	}
}
