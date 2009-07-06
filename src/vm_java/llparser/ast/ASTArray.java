package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeArray;
import vm_java.code.CodeStatement;
import vm_java.code.VMException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

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
		CodeArray ca = new CodeArray(context, source, context.intern(left.name));

		// VMArray ar = new VMArray(context);

		for (ASTVar v : mArray) {
			ca.add(context.intern(v.name));
		}
		return ca;
	}

}
