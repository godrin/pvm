package vm_java.llparser.ast;

import java.util.List;

public class RValue extends AST implements RV{
	Var var;
	Var member;
	List<Var> parameters;

	public RValue(Var v, Var m, List<Var> ps) {
		var=v;
		member=m;
		parameters=ps;
	}

}
