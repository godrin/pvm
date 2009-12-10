package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeMethodCall;
import vm_java.code.CodeStatement;
import vm_java.code.VMInternalException;
import vm_java.code.SourceBased.SourceInfo;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.llparser.LLParser2.ParseError;
import vm_java.types.basic.ObjectName;

public class ASTMethodCall extends AST implements ASTStatementInterface,
		ASTRightValue {
	String var;
	String method;
	List<ASTVar> parameters;
	boolean mstatic;

	public ASTMethodCall(SourceInfo source, ASTVar v, ASTVar m,
			List<ASTVar> ps, boolean isstatic) throws ParseError {
		super(source);
		mstatic = isstatic;
		if (v != null)
			var = v.name;

		method = m.name;
		parameters = ps;
		if (parameters == null)
			throw new ParseError();
	}

	public CodeStatement instantiate(VMContext context, ASTVar left)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMInternalException {
		List<ObjectName> ps = new ArrayList<ObjectName>();
		for (ASTVar p : parameters) {
			ps.add(p.code(context));
		}
		ObjectName returnName = null;
		if (left != null)
			returnName = left.code(context);

		return new CodeMethodCall(context, source, returnName, context
				.intern(var), context.intern(method), ps, mstatic);
	}

	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMInternalException {
		return instantiate(context, null);
	}

}
