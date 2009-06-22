package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeExpression;
import vm_java.code.UserFunction;
import vm_java.code.VMException;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.Function;
import vm_java.types.ObjectName;

public class ParsedFunction implements RV {
	List<Var> mParameters;
	ParsedBlock mBlock;

	public ParsedFunction(List<Var> parameters, ParsedBlock parsedBlock) {
		mParameters = parameters;
		mBlock = parsedBlock;
		// TODO Auto-generated constructor stub
	}

	@Override
	public BasicObject instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		List<ObjectName> ps = new ArrayList<ObjectName>();
		for (Var v : mParameters) {
			ps.add(context.intern(v.name));
		}

		Function f = new UserFunction(context, mBlock.instantiate(context), ps);

		return f;
	}

}
