package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.LocalAssignment;
import vm_java.code.VMException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.VMArray;

public class ASTArray extends AST implements ASTRightValue {

	public ASTArray(SourceInfo source) {
		super(source);
	}

	List<ASTVar> mArray = new ArrayList<ASTVar>();

	public void add(ASTVar v) {
		mArray.add(v);
	}

	@Override
	public CodeStatement instantiate(VMContext context, ASTVar left)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		VMArray ar = new VMArray(context);

		for (ASTVar v : mArray) {
			ar.add(v.code(context));
		}
		LocalAssignment la = new LocalAssignment(context, source, context
				.intern(left.name), ar);
		return la;
	}

}
