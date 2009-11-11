package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeStatement;
import vm_java.code.LocalAssignment;
import vm_java.code.UserFunction;
import vm_java.code.VMException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.Function;
import vm_java.types.foundation.ObjectName;

public class ASTFunction extends AST implements ASTRightValue {

	List<ASTVar> mParameters;
	ASTBlock mBlock;

	public ASTFunction(SourceInfo source, List<ASTVar> parameters,
			ASTBlock parsedBlock) {
		super(source);
		mParameters = parameters;
		mBlock = parsedBlock;
	}

	@Override
	public CodeStatement instantiate(VMContext context, ASTVar left)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		List<ObjectName> ps = new ArrayList<ObjectName>();
		for (ASTVar v : mParameters) {
			ps.add(context.intern(v.name));
		}
		Function f = new UserFunction(context, mBlock.instantiate(context), ps);
		
		return new LocalAssignment(context,source,
				context.intern(left.name),f);

	}
}
