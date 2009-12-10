package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.LocalAssignment;
import vm_java.code.UserFunction;
import vm_java.code.VMInternalException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.Function;
import vm_java.types.basic.ObjectName;

public class ASTFunction extends AST implements ASTRightValue {

	List<ASTVar> mParameters;
	ASTBlock mBlock;
	boolean mWithScope;

	public ASTFunction(SourceInfo source, List<ASTVar> parameters,
			ASTBlock parsedBlock, boolean withScope) {
		super(source);
		mParameters = parameters;
		mBlock = parsedBlock;
		mWithScope = withScope;
	}

	public CodeStatement instantiate(VMContext context, ASTVar left)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMInternalException {
		List<ObjectName> ps = new ArrayList<ObjectName>();
		for (ASTVar v : mParameters) {
			ps.add(context.intern(v.name));
		}
		Function f = new UserFunction(context, mBlock.instantiate(context), ps,
				mWithScope);

		return new LocalAssignment(context, source, context.intern(left.name),
				f);

	}
}
