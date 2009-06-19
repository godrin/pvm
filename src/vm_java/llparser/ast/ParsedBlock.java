package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

public class ParsedBlock {
	List<Statement> statements=new ArrayList<Statement>();

	public void add(Statement s) {
		statements.add(s);
	}

	public void instantiate() {
		
	}
}
