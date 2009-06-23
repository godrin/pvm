package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeBlock;
import vm_java.code.VMException;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class ASTBlock {
	List<ASTStatementInterface> statements=new ArrayList<ASTStatementInterface>();

	public void add(ASTStatementInterface s) {
		statements.add(s);
	}

	public CodeBlock instantiate(VMContext pContext) throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		CodeBlock cb=new CodeBlock(pContext);
		for(ASTStatementInterface s:statements) {
			System.out.println(s);
			cb.add(s.instantiate(pContext));
		}
		
		return cb;
	}
}
