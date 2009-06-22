/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import java.util.ArrayList;
import java.util.List;

import vm_java.code.IntermedResult.Result;
import vm_java.context.BasicObject;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.VMExceptionFunctionNotFound;

/**
 *
 * @author davidkamphausen
 */
public class CodeBlock extends BasicObject{
    List<CodeStatement> statements;
    private boolean isFinal=false;

    public CodeBlock(VMContext pContext) throws VMExceptionOutOfMemory {
        super(pContext);

        statements=new ArrayList<CodeStatement>();
    }


    public void add(CodeStatement pStatement) throws BlockIsFinalException, VMException {
    	if(pStatement==null)
    		throw new VMException(pStatement,"Statement is null");
    	if(isFinal)
    		throw new BlockIsFinalException();
        statements.add(pStatement);
    }

    IntermedResult execute(VMScope pScope) throws VMException, VMExceptionOutOfMemory, VMExceptionFunctionNotFound {
    	IntermedResult result;
        for(CodeStatement s:statements) {
        	if(s==null)
        		throw new VMException(s,"Statement is null");
        	System.out.println("exec:"+s);
            result=s.execute(pScope);
            if(result==null)
        		throw new VMException(s,"result is null");
            	
            if(result.broken())
            	return result;
        }
		return new IntermedResult(BasicObject.nil,Result.NONE);
    }

	public CodeStatement getStatement(int currentLine) {
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
