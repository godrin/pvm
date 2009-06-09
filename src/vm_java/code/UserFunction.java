/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.context.VMScope;

/**
 *
 * @author davidkamphausen
 */
public class UserFunction extends Function {

    UserFunction(Block pBlock) {
        super(pBlock.getContext());
        mBlock = pBlock;
    }

    public void execute(VMScope pScope) {
        mBlock.execute(pScope);
    }
    Block mBlock;
}
