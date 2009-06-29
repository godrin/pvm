package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.BlockIsFinalException;
import vm_java.code.CodeMethodCall;
import vm_java.code.VMException;
import vm_java.code.CodeStatement.SourceInfo;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.llparser.LLParser2.ParseError;
import vm_java.types.ObjectName;

//FIXME: rename to method-call
@Deprecated
public class RValue extends AST implements ASTRightValue {
	ASTVar var;
	ASTVar member;
	List<ASTVar> parameters;

	public RValue(SourceInfo source, ASTVar v, ASTVar m, List<ASTVar> ps) throws ParseError {
		super(source);
		var = v;
		member = m;
		parameters = ps;
		if (var == null)
			throw new ParseError();
	}

	@Override
	public BasicObject instantiate(VMContext context)
			throws VMExceptionOutOfMemory, BlockIsFinalException, VMException {
		ObjectName cvar;
		// if (var != null) {
		cvar = context.intern(var.name);
		// }
		ObjectName cmember = context.intern(member.name);
		List<BasicObject> ps = new ArrayList<BasicObject>();
		for (ASTVar v : parameters) {
			ps.add(v.instantiate(context));
		}
		return new CodeMethodCall(context, source, cvar, cmember, ps);
	}

}
