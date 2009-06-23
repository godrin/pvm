package vm_java.llparser.ast;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeBlock;
import vm_java.code.Program;
import vm_java.code.VMException;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class ASTProgram {
	ASTBlock mBlock = null;

	public void setBlock(ASTBlock block) {
		mBlock=block;
		
	}
	
	public Program instantiate(VMContext context) throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		CodeBlock block=mBlock.instantiate(context);
		return new Program(context,block);
	}


}
