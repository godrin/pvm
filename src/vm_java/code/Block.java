/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.Statement.Result;
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

    Result execute(VMScope pScope) throws VMException {
    	Result result;
        for(Statement s:statements) {
            result=s.execute(pScope);
            if(result!=Result.NONE)
            	return result;
        }
		return Result.NONE;
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
