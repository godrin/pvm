package vm_java.llparser.ast;

import java.util.ArrayList;
import java.util.List;

public class Program {
	List<ParsedBlock> blocks = new ArrayList<ParsedBlock>();

	public void add(ParsedBlock block) {
		blocks.add(block);
		
	}

}
