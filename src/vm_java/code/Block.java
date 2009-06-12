/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import java.util.ArrayList;
import java.util.List;
import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.context.VMContext;

/**
 *
 * @author davidkamphausen
 */
public class Block extends BasicObject{
    List<Statement> statements;
    private boolean isFinal=false;

    public Block(VMContext pContext) throws VMExceptionOutOfMemory {
        super(pContext);

        statements=new ArrayList<Statement>();
    }


    public void add(Statement pStatement) throws BlockIsFinalException {
    	if(isFinal)
    		throw new BlockIsFinalException();
        statements.add(pStatement);
    }

    void execute(VMScope pScope) throws VMException {
        for(Statement s:statements)
            s.execute(pScope);
    }

	public Statement getStatement(int currentLine) {
		isFinal=true;
		if(currentLine>=0 && currentLine<statements.size()) {
			return statements.get(currentLine);
		}
		return null;
	}


	public int size() {
		isFinal=true;
		return statements.size();
	}
}
