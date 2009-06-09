/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java.code;

import vm_java.context.BasicObject;
import vm_java.context.VMScope;
import vm_java.context.VMContext;

/**
 *
 * @author davidkamphausen
 */
public class Program extends BasicObject{

    public Program(VMContext pContext,Block pBlock) {
        super(pContext);
        mBlock=pBlock;
    }

    public void execute(VMScope pScope) {
        mBlock.execute(pScope);
    }

    Block mBlock;
}
