/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.context.VMContext;

/**
 *
 * @author davidkamphausen
 */
public class Program extends BasicObject{

    private CodeBlock mBlock;

    public Program(VMContext pContext,CodeBlock pBlock) throws VMExceptionOutOfMemory {
        super(pContext);
        mBlock=pBlock;
    }

    public void execute(VMScope pScope) throws VMException {
        mBlock.execute(pScope);
    }

	public CodeBlock getBlock() {
		return mBlock;
	}
}
