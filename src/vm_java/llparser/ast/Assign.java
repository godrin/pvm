package vm_java.llparser.ast;

public class Assign extends AST implements Statement {
	public Var left;
	public RV right;

	public Assign(Var pleft, RV pright) {
		left = pleft;
		right = pright;
	}
}
