package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeMethodCall;
import vm_java.code.CodeStatement;
import vm_java.code.VMException;
import vm_java.code.CodeStatement.SourceInfo;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;

public class ASTMethodCall extends AST implements ASTStatementInterface, ASTRightValue {
	String var;
	String method;
	List<ASTVar> parameters;

	public ASTMethodCall(SourceInfo source,ASTVar v, ASTVar m, List<ASTVar> ps) {
		super(source);
		if (v != null)
			var = v.name;

		method = m.name;
		parameters = ps;
	}

	@Override
	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		List<BasicObject> ps = new ArrayList<BasicObject>();
		for (ASTVar p : parameters) {
			ps.add(p.instantiate(context));
		}
		return new CodeMethodCall(context, source, context.intern(var), context
				.intern(method), ps);
	}

}
