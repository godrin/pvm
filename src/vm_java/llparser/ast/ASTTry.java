package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeBlock;
import vm_java.code.CodeRescue;
import vm_java.code.CodeStatement;
import vm_java.code.CodeTry;
import vm_java.code.VMInternalException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class ASTTry extends AST implements ASTStatementInterface{

	ASTBlock block;
	Collection<ASTRescue> rescues;

	public ASTTry(SourceInfo pSource, ASTBlock pblock, List<ASTRescue> prescues) {
		super(pSource);
		block = pblock;
		rescues = prescues;
	}

	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMInternalException {
		CodeBlock mblock = block.instantiate(context);

		List<CodeRescue> mrescues = new ArrayList<CodeRescue>();
		for (ASTRescue rescue : rescues) {
			mrescues.add(rescue.instantiate(context));
		}

		CodeTry mtry = new CodeTry(context, source, mblock, mrescues);
		return mtry;
	}

}
