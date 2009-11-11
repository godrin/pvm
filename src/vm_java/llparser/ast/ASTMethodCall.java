package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeMethodCall;
import vm_java.code.CodeStatement;
import vm_java.code.VMException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.llparser.LLParser2.ParseError;
import vm_java.types.ObjectName;

public class ASTMethodCall extends AST implements ASTStatementInterface,
		ASTRightValue {
	String var;
	String method;
	List<ASTVar> parameters;

	public ASTMethodCall(SourceInfo source, ASTVar v, ASTVar m, List<ASTVar> ps)
			throws ParseError {
		super(source);
		if (v != null)
			var = v.name;

		method = m.name;
		parameters = ps;
		if (parameters == null)
			throw new ParseError();
	}

	@Override
	public CodeStatement instantiate(VMContext context, ASTVar left)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		List<ObjectName> ps = new ArrayList<ObjectName>();
		for (ASTVar p : parameters) {
			ps.add(p.code(context));
		}
		ObjectName returnName = null;
		if (left != null)
			returnName = left.code(context);

		return new CodeMethodCall(context, source, returnName, context
				.intern(var), context.intern(method), ps);
	}

	@Override
	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		return instantiate(context, null);
	}

}
