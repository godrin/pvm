package vm_java.llparser.ast;

import vm_java.code.CodeExpression;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.VMString;

public class ASTString implements RV{
	String s;

	public ASTString(String string) {
		s = string;
	}

	@Override
	public BasicObject instantiate(VMContext context) throws VMExceptionOutOfMemory {
		return new VMString(context,s);
	}

}
