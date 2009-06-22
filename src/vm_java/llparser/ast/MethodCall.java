package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.CodeMethodCall;
import vm_java.code.CodeStatement;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.types.ObjectName;

public class MethodCall implements Statement {
	String var;
	String method;
	List<Var> parameters;

	public MethodCall(Var v, Var m, List<Var> ps) {
		if (v != null)
			var = v.name;

		method = m.name;
		parameters = ps;
	}

	@Override
	public CodeStatement instantiate(VMContext context)
			throws VMExceptionOutOfMemory {
		List<ObjectName> ps = new ArrayList<ObjectName>();
		for (Var p : parameters) {
			ps.add(context.intern(p.name));
		}
		return new CodeMethodCall(context, context.intern(var), context
				.intern(method), ps);
	}

}
