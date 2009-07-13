package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeBlock;
import vm_java.code.CodeStatement;
import vm_java.code.LocalAssignment;
import vm_java.code.VMException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.llparser.LLParser2;
import vm_java.llparser.LLParser2.ParseError;

public class ASTBlock extends AST implements ASTRightValue {
	public ASTBlock(SourceInfo source) {
		super(source);
	}

	List<ASTStatementInterface> statements = new ArrayList<ASTStatementInterface>();

	public void add(ASTStatementInterface s) throws ParseError {
		if (s == null) {
			throw new LLParser2.ParseError();
		}
		statements.add(s);
	}

	public CodeBlock instantiate(VMContext pContext)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		CodeBlock cb = new CodeBlock(pContext, source);
		for (ASTStatementInterface s : statements) {
			cb.add(s.instantiate(pContext));
		}

		return cb;
	}

	@Override
	public CodeStatement instantiate(VMContext pContext, ASTVar left)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		CodeBlock cb = new CodeBlock(pContext, source);
		for (ASTStatementInterface s : statements) {
			cb.add(s.instantiate(pContext));
		}
		
		return new LocalAssignment(pContext,source,
				pContext.intern(left.name),cb);
	}
}
