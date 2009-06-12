/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;

/**
 *
 * @author davidkamphausen
 */
public class UserFunction extends Function {

    UserFunction(Block pBlock) throws VMExceptionOutOfMemory {
        super(pBlock.getContext());
        mBlock = pBlock;
    }

    @Override
    public void execute(VMScope pScope) throws VMException {
        mBlock.execute(pScope);
    }
    Block mBlock;
}
