package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeResolveVar;
import vm_java.code.VMException;
import vm_java.code.CodeStatement.SourceInfo;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class ASTVar extends AST implements ASTRightValue {
	public String name;

	public ASTVar(SourceInfo source,String pname) {
		super(source);
		name = pname;
	}

	@Override
	public BasicObject instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		return new CodeResolveVar(context,context.intern(name));
	}
}