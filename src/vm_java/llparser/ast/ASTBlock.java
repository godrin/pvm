package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeBlock;
import vm_java.code.VMException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.internal.VMLog;

public class ASTBlock extends AST{
	public ASTBlock(SourceInfo source) {
		super(source);
	}

	List<ASTStatementInterface> statements=new ArrayList<ASTStatementInterface>();

	public void add(ASTStatementInterface s) {
		statements.add(s);
	}

	public CodeBlock instantiate(VMContext pContext) throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		CodeBlock cb=new CodeBlock(pContext,source);
		for(ASTStatementInterface s:statements) {
			VMLog.debug(s);
			cb.add(s.instantiate(pContext));
		}
		
		return cb;
	}
}
