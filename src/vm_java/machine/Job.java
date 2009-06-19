package vm_java.machine;

import vm_java.code.CodeBlock;
import vm_java.code.Program;
import vm_java.code.Statement;
import vm_java.context.VMContext;
import vm_java.context.VMScope;

public class Job {

	VMContext context;
	CodeBlock block;
	VMScope scope;
	int currentLine;

	public Job(Program pProgram, VMScope pScope) {
		context = pProgram.getContext();
		block = pProgram.getBlock();
		scope = pScope;
		currentLine = 0;
		// TODO Auto-generated constructor stub
	}
	
	Statement getNextStatement() {
		Statement statement= block.getStatement(currentLine);
		currentLine +=1;
		return statement;
	}

	public boolean finished() {
		return (currentLine>=block.size());
	}

	public VMScope getScope() {
		return scope;
	}

}
