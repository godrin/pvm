package vm_java.llparser.ast;

import java.util.List;

import vm_java.code.CodeExpression;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;

//FIXME: rename to method-call
public class RValue extends AST implements RV{
	Var var;
	Var member;
	List<Var> parameters;

	public RValue(Var v, Var m, List<Var> ps) {
		var=v;
		member=m;
		parameters=ps;
	}

	@Override
	public BasicObject instantiate(VMContext context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
