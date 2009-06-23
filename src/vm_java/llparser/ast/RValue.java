package vm_java.llparser.ast;

import java.util.List;

import vm_java.context.BasicObject;
import vm_java.context.VMContext;

//FIXME: rename to method-call
@Deprecated
public class RValue extends AST implements ASTRightValue{
	ASTVar var;
	ASTVar member;
	List<ASTVar> parameters;

	public RValue(ASTVar v, ASTVar m, List<ASTVar> ps) {
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
