package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.BlockIsFinalException;
import vm_java.code.UserFunction;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.Function;
import vm_java.types.ObjectName;

public class ASTFunction implements ASTRightValue {
	List<ASTVar> mParameters;
	ASTBlock mBlock;

	public ASTFunction(List<ASTVar> parameters, ASTBlock parsedBlock) {
		mParameters = parameters;
		mBlock = parsedBlock;
		// TODO Auto-generated constructor stub
	}

	@Override
	public BasicObject instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		List<ObjectName> ps = new ArrayList<ObjectName>();
		for (ASTVar v : mParameters) {
			ps.add(context.intern(v.name));
		}

		Function f = new UserFunction(context, mBlock.instantiate(context), ps);

		return f;
	}

}
