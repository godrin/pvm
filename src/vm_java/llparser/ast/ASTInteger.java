package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.VMInteger;

public class ASTInteger implements ASTRightValue {
	int i;

	public ASTInteger(String string) {
		i=Integer.parseInt(string);
	}

	@Override
	public BasicObject instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		return new VMInteger(context,i);
	}

}
