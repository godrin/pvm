package vm_java.llparser.ast;

import java.util.List;

public class MethodCall implements Statement{
	Var var;
	Var method;
	List<Var> parameters;

	public MethodCall(Var v, Var m, List<Var> ps) {
		var=v;
		method=m;
		parameters=ps;
	}

}
